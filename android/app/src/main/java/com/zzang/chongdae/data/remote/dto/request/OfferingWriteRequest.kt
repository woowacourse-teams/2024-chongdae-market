package com.zzang.chongdae.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferingWriteRequest(
    @SerialName("memberId") val memberId: Long,
    @SerialName("title") val title: String,
    @SerialName("productUrl") val productUrl: String?,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("totalPrice") val totalPrice: Int,
    @SerialName("eachPrice") val eachPrice: Int,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("meetingAddressDong") val meetingAddressDong: String?,
    @SerialName("meetingAddressDetail") val meetingAddressDetail: String,
    @SerialName("deadline") val deadline: String,
    @SerialName("description") val description: String,
)
