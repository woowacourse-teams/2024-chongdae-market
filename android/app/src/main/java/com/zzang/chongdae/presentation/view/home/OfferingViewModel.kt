package com.zzang.chongdae.presentation.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OfferingViewModel(
    private val offeringRepository: OfferingRepository,
    private val authRepository: AuthRepository,
) : ViewModel(), OnFilterClickListener, OnSearchClickListener {
    private val _offerings = MutableLiveData<PagingData<Offering>>()
    val offerings: LiveData<PagingData<Offering>> get() = _offerings

    val search: MutableLiveData<String?> = MutableLiveData(null)

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

    init {
        fetchOfferings()
        fetchFilters()
    }

    private fun fetchOfferings() {
        Log.e("seogi", "fetchOfferings")
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = {
                    OfferingPagingSource(
                        offeringRepository,
                        authRepository,
                        search.value,
                        _selectedFilter.value,
                        { fetchOfferings() },
                    )
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
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            fetchFilters()
                        }
                        else -> DataError.Network.UNKNOWN
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
                            authRepository.saveRefresh()
                            fetchUpdatedOffering(offeringId)
                        }
                        else -> DataError.Network.UNKNOWN
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

    fun refreshOfferingsByOfferingWriteEvent(isSuccess: Boolean) {
        if (isSuccess) {
            search.value = null
            _selectedFilter.value = null
            _offeringsRefreshEvent.setValue(Unit)
            fetchOfferings()
        }
    }

    companion object {
        private const val PAGE_SIZE = 10

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringRepository: OfferingRepository,
            authRepository: AuthRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingViewModel(
                    offeringRepository,
                    authRepository,
                ) as T
            }
        }
    }
}
