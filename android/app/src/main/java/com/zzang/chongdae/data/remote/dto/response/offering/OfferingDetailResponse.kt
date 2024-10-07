package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferingDetailResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("productUrl") val productUrl: String?,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("meetingAddressDetail") val meetingAddressDetail: String,
    @SerialName("description") val description: String,
    @SerialName("meetingDate") val meetingDate: String,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("totalPrice") val totalPrice: Int,
    @SerialName("originPrice") val originPrice: Int?,
    @SerialName("status") val condition: RemoteOfferingStatus,
    @SerialName("isProposer") val isProposer: Boolean,
    @SerialName("nickname") val nickname: String,
    @SerialName("isParticipated") val isParticipated: Boolean,
)
