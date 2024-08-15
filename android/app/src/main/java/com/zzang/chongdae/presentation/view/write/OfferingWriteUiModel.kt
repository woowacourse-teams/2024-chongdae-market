package com.zzang.chongdae.presentation.view.write

data class OfferingWriteUiModel(
    val title: String,
    val productUrl: String?,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val totalPrice: Int,
    val originPrice: Int?,
    val meetingAddress: String,
    val meetingAddressDong: String?,
    val meetingAddressDetail: String,
    val deadline: String,
    val description: String,
)
