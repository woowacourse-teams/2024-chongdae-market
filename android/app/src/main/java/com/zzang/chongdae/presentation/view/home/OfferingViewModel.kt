package com.zzang.chongdae.presentation.view.home

import android.util.Log
import android.view.View
import android.widget.RadioButton
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
import com.zzang.chongdae.domain.model.toOffering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OfferingViewModel(
    private val offeringRepository: OfferingRepository,
    private val offeringDetailRepository: OfferingDetailRepository,
) : ViewModel(), OnFilterClickListener, OnSearchClickListener {
    private val _offerings = MutableLiveData<PagingData<Offering>>()
    val offerings: LiveData<PagingData<Offering>> get() = _offerings

    val search: MutableLiveData<String?> = MutableLiveData(null)

    private val _filters: MutableLiveData<List<Filter>> = MutableLiveData()
    val filters: MutableLiveData<List<Filter>> get() = _filters

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

    private val selectedFilter = MutableLiveData<String?>()

    private val _searchEvent: MutableSingleLiveData<String?> = MutableSingleLiveData(null)
    val searchEvent: SingleLiveData<String?> get() = _searchEvent

    private val _filterOfferingsEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
    val filterOfferingsEvent: SingleLiveData<Unit> get() = _filterOfferingsEvent

    private val _updatedOffering: MutableLiveData<Offering> = MutableLiveData()
    val updatedOffering: MutableLiveData<Offering> get() = _updatedOffering

    init {
        fetchOfferings()
        fetchFilters()
    }

    private fun fetchOfferings() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = {
                    OfferingPagingSource(offeringRepository, search.value, selectedFilter.value)
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
            offeringRepository.fetchFilters().onSuccess {
                _filters.value = it
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    override fun onClickFilter(
        filterName: FilterName,
        button: View,
    ) {
        val isChecked = (button as RadioButton).isChecked
        // 현재 서버에서 참여가능만 필터 기능이 구현되지 않아 임시로 분기처리
        if (filterName == FilterName.JOINABLE) return
        if (selectedFilter.value == filterName.toString()) {
            selectedFilter.value = null
            button.isChecked = false
        } else if (isChecked) {
            selectedFilter.value = filterName.toString()
        } else {
            selectedFilter.value = null
        }
        _filterOfferingsEvent.setValue(Unit)
        fetchOfferings()
    }

    override fun onClickSearchButton() {
        _searchEvent.setValue(search.value)
        fetchOfferings()
    }

    fun fetchOfferingDetail(offeringId: Long) {
        viewModelScope.launch {
            offeringDetailRepository.fetchOfferingDetail(
                offeringId = offeringId,
            ).onSuccess {
                _updatedOffering.value = it.toOffering()
            }.onFailure {
                Log.e("Error", it.message.toString())
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringRepository: OfferingRepository,
            offeringDetailRepository: OfferingDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingViewModel(
                    offeringRepository,
                    offeringDetailRepository,
                ) as T
            }
        }
    }
}
