package com.zzang.chongdae.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferingWriteRequest(
    @SerialName("title") val title: String,
    @SerialName("productUrl") val productUrl: String?,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("totalPrice") val totalPrice: Int,
    @SerialName("originPrice") val originPrice: Int?,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("meetingAddressDong") val meetingAddressDong: String?,
    @SerialName("meetingAddressDetail") val meetingAddressDetail: String,
    @SerialName("meetingDate") val meetingDate: String,
    @SerialName("description") val description: String,
)
