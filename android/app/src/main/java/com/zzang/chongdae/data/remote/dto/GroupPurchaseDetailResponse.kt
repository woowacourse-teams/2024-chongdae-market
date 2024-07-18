package com.zzang.chongdae.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupPurchaseDetailResponse(
    val id: Long,
    val title: String,
    val nickname: String,
    val productUrl: String,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val description: String,
    val deadline: String,
    val currentCount: Int,
    val totalCount: Int,
    val thumbnailUrl: String,
    val dividedPrice: Int,
    val totalPrice: Int,
    val status: PurchaseStatus,
)
