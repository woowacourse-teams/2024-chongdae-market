package com.zzang.chongdae.presentation.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.paging.OfferingPagingSource
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OfferingViewModel(
    private val offeringRepository: OfferingRepository,
) : ViewModel(), OnFilterClickListener {
    private val _offerings = MutableLiveData<PagingData<Offering>>()
    val offerings: LiveData<PagingData<Offering>> get() = _offerings

    val search: MutableLiveData<String?> = MutableLiveData(null)

    private val selectedFilter = MutableLiveData<String?>()


    init {
        fetchOfferings()
    }

    fun fetchOfferings() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = {
                    OfferingPagingSource(offeringRepository, search.value, selectedFilter.value)
                },
            ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                _offerings.value = pagingData
            }
        }
    }

    override fun onClickFilter(type: String, isChecked: Boolean) {
        if (isChecked) {
            selectedFilter.value = type.toFilterCondition()
        } else {
            selectedFilter.value = null
        }
        fetchOfferings()
    }

    private fun String.toFilterCondition() = when (this) {
        "참여가능만" -> "JOINABLE"
        "마감임박순" -> "IMMINENT"
        "높은할인율순" -> "HIGH_DISCOUNT"
        else -> null
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
