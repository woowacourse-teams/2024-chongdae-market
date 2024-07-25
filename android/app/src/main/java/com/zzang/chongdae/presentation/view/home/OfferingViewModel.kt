package com.zzang.chongdae.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingsRepository

class OfferingViewModel(
    private val offeringsRepository: OfferingsRepository,
) : ViewModel() {
    val offerings: LiveData<PagingData<Offering>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { OfferingPagingSource(fetchOfferings = offeringsRepository::fetchOfferings) },
        ).liveData
            .cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}
