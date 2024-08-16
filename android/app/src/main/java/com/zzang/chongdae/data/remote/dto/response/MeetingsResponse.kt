package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingsResponse(
    @SerialName("meetingDate")
    val meetingDate: String,
    @SerialName("meetingAddress")
    val meetingAddress: String,
    @SerialName("meetingAddressDetail")
    val meetingAddressDetail: String,
    @SerialName("meetingAddressDong")
    val meetingAddressDong: String?,
)
