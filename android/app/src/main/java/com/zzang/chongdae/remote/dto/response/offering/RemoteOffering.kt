package com.zzang.chongdae.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOffering(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("meetingAddressDong") val meetingAddressDong: String?,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("originPrice") val originPrice: Int?,
    @SerialName("discountRate") val discountRate: Float?,
    @SerialName("status") val status: RemoteOfferingStatus,
    @SerialName("isOpen") val isOpen: Boolean,
)
