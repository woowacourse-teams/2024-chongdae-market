package com.zzang.chongdae.presentation.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zzang.chongdae.domain.repository.OfferingsRepository

class OfferingViewModeFactory(
    private val offeringsRepository: OfferingsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OfferingViewModel(offeringsRepository) as T
    }
}
