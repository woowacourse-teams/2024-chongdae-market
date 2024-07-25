package com.zzang.chongdae.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingsRepository
import kotlinx.coroutines.launch

class OfferingViewModel(
    private val offeringsRepository: OfferingsRepository,
) : ViewModel() {
    private val _offerings: MutableLiveData<List<Offering>> = MutableLiveData()
    val offerings: LiveData<List<Offering>> get() = _offerings

    // 무한 스크롤 적용 시 수정 예정
    fun updateArticles() {
        viewModelScope.launch {
            offeringsRepository.fetchOfferings(0L, 10).onSuccess {
                _offerings.value = it
            }.onFailure {
            }
        }
    }
}
