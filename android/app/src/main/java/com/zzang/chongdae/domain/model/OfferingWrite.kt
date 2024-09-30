package com.zzang.chongdae.domain.model

data class OfferingWrite(
    val title: String,
    val productUrl: String?,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val totalPrice: Int,
    val originPrice: Int?,
    val meetingAddress: String,
    val meetingAddressDong: String?,
    val meetingAddressDetail: String,
    val meetingDate: String,
    val description: String,
)
