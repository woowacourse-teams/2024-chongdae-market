package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingsResponse(
    @SerialName("deadline")
    val deadline: String,
    @SerialName("meetingAddress")
    val meetingAddress: String,
    @SerialName("meetingAddressDetail")
    val meetingAddressDetail: String,
)
