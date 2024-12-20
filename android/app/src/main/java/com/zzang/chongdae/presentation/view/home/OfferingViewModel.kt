package com.zzang.chongdae.presentation.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.zzang.chongdae.R
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferingViewModel
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : ViewModel(), OnFilterClickListener, OnSearchClickListener {
        private val _offerings = MutableLiveData<PagingData<Offering>>()
        val offerings: LiveData<PagingData<Offering>> get() = _offerings

        val search: MutableLiveData<String?> = MutableLiveData(null)
        val isSearchKeywordExist = search.map { (it != null) && (it != "") }

        private val _filters: MutableLiveData<List<Filter>> = MutableLiveData()
        val filters: LiveData<List<Filter>> get() = _filters

        val joinableFilter: LiveData<Filter> =
            _filters.map {
                it.first { it.name == FilterName.JOINABLE }
            }

        val imminentFilter: LiveData<Filter> =
            _filters.map {
                it.first { it.name == FilterName.IMMINENT }
            }

        val highDiscountFilter: LiveData<Filter> =
            _filters.map {
                it.first { it.name == FilterName.HIGH_DISCOUNT }
            }

        private val _selectedFilter: MutableLiveData<String?> = MutableLiveData<String?>()
        val selectedFilter: LiveData<String?> get() = _selectedFilter

        private val _searchEvent: MutableSingleLiveData<String?> = MutableSingleLiveData(null)
        val searchEvent: SingleLiveData<String?> get() = _searchEvent

        private val _filterOfferingsEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val filterOfferingsEvent: SingleLiveData<Unit> get() = _filterOfferingsEvent

        private val _updatedOffering: MutableSingleLiveData<MutableList<Offering>> =
            MutableSingleLiveData(mutableListOf())
        val updatedOffering: SingleLiveData<MutableList<Offering>> get() = _updatedOffering

        private val _offeringsRefreshEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val offeringsRefreshEvent: SingleLiveData<Unit> get() = _offeringsRefreshEvent

        private val _error: MutableSingleLiveData<Int> = MutableSingleLiveData()
        val error: SingleLiveData<Int> get() = _error

        private val _refreshTokenExpiredEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val refreshTokenExpiredEvent: SingleLiveData<Unit> get() = _refreshTokenExpiredEvent

        init {
            fetchFilters()
            fetchOfferings()
        }

        private fun fetchOfferings() {
            viewModelScope.launch {
                Pager(
                    config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                    pagingSourceFactory = {
                        OfferingPagingSource(
                            offeringRepository,
                            authRepository,
                            search.value,
                            _selectedFilter.value,
                        ) { fetchOfferings() }
                    },
                ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                    _offerings.value =
                        pagingData.map {
                            if (isSearchKeywordExist() && isTitleContainSearchKeyword(it)) {
                                return@map it.copy(
                                    title =
                                        highlightSearchKeyword(
                                            it.title,
                                            search.value!!,
                                        ),
                                )
                            }
                            it.copy(title = removeAsterisks(it.title))
                        }
                }
            }
        }

        private fun removeAsterisks(title: String): String {
            return title.replace("*", "")
        }

        private fun highlightSearchKeyword(
            title: String,
            keyword: String,
        ): String {
            return title.replace(keyword, "*$keyword*")
        }

        private fun isTitleContainSearchKeyword(it: Offering) = (search.value as String) in it.title

        private fun isSearchKeywordExist() = (search.value != null) && (search.value != "")

        private fun fetchFilters() {
            viewModelScope.launch {
                when (val result = offeringRepository.fetchFilters()) {
                    is Result.Error -> {
                        Log.d("error", "fetchFilters: ${result.error}")
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> fetchFilters()
                                    is Result.Error -> {
                                        userPreferencesDataStore.removeAllData()
                                        _refreshTokenExpiredEvent.setValue(Unit)
                                        return@launch
                                    }
                                }
                            }

                            DataError.Network.BAD_REQUEST -> {
                                _error.setValue(R.string.home_filter_error_message)
                            }

                            else -> {
                                Log.e("error", "fetchFilters Error: ${result.error.name}")
                            }
                        }
                    }

                    is Result.Success -> {
                        _filters.value = result.data
                    }
                }
            }
        }

        override fun onClickFilter(
            filterName: FilterName,
            isChecked: Boolean,
        ) {
            if (isChecked) {
                _selectedFilter.value = filterName.toString()
            } else {
                _selectedFilter.value = null
            }

            _filterOfferingsEvent.setValue(Unit)
            fetchOfferings()
        }

        override fun onClickSearchButton() {
            _searchEvent.setValue(search.value)
            fetchOfferings()
        }

        fun fetchUpdatedOffering(offeringId: Long) {
            viewModelScope.launch {
                when (val result = offeringRepository.fetchOffering(offeringId)) {
                    is Result.Error -> {
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> fetchUpdatedOffering(offeringId)
                                    is Result.Error -> return@launch
                                }
                            }

                            DataError.Network.BAD_REQUEST -> {
                                _error.setValue(R.string.home_updated_offering_error_mesasge)
                            }

                            else -> {
                                Log.e("error", "fetchUpdatedOffering Error: ${result.error.name}")
                            }
                        }
                    }

                    is Result.Success -> {
                        val updatedOfferings = _updatedOffering.getValue() ?: mutableListOf()
                        updatedOfferings.add(result.data)
                        _updatedOffering.setValue(updatedOfferings)
                    }
                }
            }
        }

        fun refreshOfferings(isSuccess: Boolean) {
            if (isSuccess) {
                search.value = null
                _selectedFilter.value = null
                _offeringsRefreshEvent.setValue(Unit)
                fetchOfferings()
            }
        }

        fun swipeRefresh() {
            _offeringsRefreshEvent.setValue(Unit)
            fetchOfferings()
        }

        companion object {
            private const val PAGE_SIZE = 10
        }
    }
