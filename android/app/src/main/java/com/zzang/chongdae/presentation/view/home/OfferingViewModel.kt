package com.zzang.chongdae.presentation.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zzang.chongdae.R
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.usecase.home.FetchFiltersUserCase
import com.zzang.chongdae.domain.usecase.home.FetchOfferingUseCase
import com.zzang.chongdae.domain.usecase.home.FetchOfferingsUseCase
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferingViewModel
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        private val fetchOfferingsUseCase: FetchOfferingsUseCase,
        private val fetchFiltersUserCase: FetchFiltersUserCase,
        private val fetchOfferingUseCase: FetchOfferingUseCase,
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : ViewModel(), OnFilterClickListener, OnSearchClickListener {
        private val _offerings = MutableStateFlow<PagingData<Offering>>(value = PagingData.empty())
        val offerings: StateFlow<PagingData<Offering>> get() = _offerings

        val search: MutableLiveData<String?> = MutableLiveData(null)

        private val _filters: MutableLiveData<List<Filter>> = MutableLiveData()
        val filters: LiveData<List<Filter>> get() = _filters

        private val _selectedFilter: MutableLiveData<Filter?> = MutableLiveData<Filter?>()
        val selectedFilter: LiveData<Filter?> get() = _selectedFilter

        private val _searchEvent: MutableLiveData<String?> = MutableLiveData(null)
        val searchEvent: LiveData<String?> get() = _searchEvent

        private val _updatedOffering: MutableLiveData<MutableList<Offering>> =
            MutableLiveData(mutableListOf())
        val updatedOffering: LiveData<MutableList<Offering>> get() = _updatedOffering

        private val _error: MutableSingleLiveData<Int> = MutableSingleLiveData()
        val error: SingleLiveData<Int> get() = _error

        private val _refreshTokenExpiredEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val refreshTokenExpiredEvent: SingleLiveData<Unit> get() = _refreshTokenExpiredEvent

        init {
            fetchFilters()
            fetchOfferings()
        }

        fun updateSearch(newText: String) {
            search.value = newText
        }

        private fun fetchOfferings() {
            viewModelScope.launch {
                Pager(
                    config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                    pagingSourceFactory = {
                        OfferingPagingSource(
                            fetchOfferingsUseCase,
                            authRepository,
                            search.value,
                            _selectedFilter.value?.name?.toString(),
                        ) { fetchOfferings() }
                    },
                ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                    _offerings.value =
                        pagingData
                }
            }
        }

        private fun fetchFilters() {
            viewModelScope.launch {
                when (val result = fetchFiltersUserCase()) {
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
            filter: Filter,
            isChecked: Boolean,
        ) {
            if (isChecked) {
                _selectedFilter.value = filter
            } else {
                _selectedFilter.value = null
            }
            fetchOfferings()
        }

        override fun onClickSearchButton() {
            _searchEvent.value = search.value
            fetchOfferings()
        }

        fun fetchUpdatedOffering(offeringId: Long) {
            viewModelScope.launch {
                when (val result = fetchOfferingUseCase(offeringId)) {
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
                _searchEvent.value = null
                fetchOfferings()
            }
        }

        fun swipeRefresh() {
            fetchOfferings()
        }

        companion object {
            private const val PAGE_SIZE = 10
        }
    }
