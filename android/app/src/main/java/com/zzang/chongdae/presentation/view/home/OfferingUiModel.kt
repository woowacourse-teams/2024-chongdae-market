package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.domain.model.Offering

data class OfferingUiModel(
    val offering: Offering,
    val isSearched: Boolean = false,
)

fun Offering.toUiModel() =
    OfferingUiModel(
        offering = this,
    )
