package com.zzang.chongdae.presentation.view.write

data class OfferingWriteUiState(
    val memberId: Long,
    val title: String,
    val productUrl: String?,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val totalPrice: Int,
    val eachPrice: Int,
    val meetingAddress: String,
    val meetingAddressDong: String?,
    val meetingAddressDetail: String,
    val deadline: String,
    val description: String,
)
