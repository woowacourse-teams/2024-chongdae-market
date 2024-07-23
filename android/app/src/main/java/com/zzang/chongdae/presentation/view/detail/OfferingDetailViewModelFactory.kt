package com.zzang.chongdae.presentation.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailViewModelFactory(
    private val articleId: Long,
    private val offeringDetailRepository: OfferingDetailRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OfferingDetailViewModel(
            articleId,
            offeringDetailRepository,
        ) as T
    }
}
