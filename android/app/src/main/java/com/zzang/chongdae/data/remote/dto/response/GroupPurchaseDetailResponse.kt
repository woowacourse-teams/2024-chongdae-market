package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupPurchaseDetailResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("productUrl") val productUrl: String,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("meetingAddressDetail") val meetingAddressDetail: String,
    @SerialName("description") val description: String,
    @SerialName("deadline") val deadline: String,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("totalPrice") val totalPrice: Int,
    @SerialName("status") val status: PurchaseStatus,
)
