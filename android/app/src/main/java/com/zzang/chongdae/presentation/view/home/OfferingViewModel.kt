package com.zzang.chongdae.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingRepository

class OfferingViewModel(
    private val offeringRepository: OfferingRepository,
) : ViewModel() {
    lateinit var offerings: LiveData<PagingData<Offering>>
        private set

    init {
        getOfferings()
    }

    private fun getOfferings() {
        offerings =
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = { OfferingPagingSource(fetchOfferings = offeringRepository::fetchOfferings) },
            ).liveData.cachedIn(viewModelScope)
    }

    companion object {
        private const val PAGE_SIZE = 10

        @Suppress("UNCHECKED_CAST")
        fun getFactory(offeringRepository: OfferingRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return OfferingViewModel(
                        offeringRepository,
                    ) as T
                }
            }
    }
}
