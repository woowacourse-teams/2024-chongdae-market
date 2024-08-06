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
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OfferingViewModel(
    private val offeringRepository: OfferingRepository,
) : ViewModel() {
    private val _offerings = MutableLiveData<PagingData<Offering>>()
    val offerings: LiveData<PagingData<Offering>> = _offerings
    init {
        getOfferings()
    }

    private fun getOfferings() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = {
                    OfferingPagingSource(offeringRepository)
                },
            ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                _offerings.value = pagingData
            }
        }
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
