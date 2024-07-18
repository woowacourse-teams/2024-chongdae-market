package com.zzang.chongdae.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupPurchase(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("deadline") val deadline: String,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("isClosed") val isClosed: Boolean,
)
