package com.zzang.chongdae.data.remote.dto.response.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserTypeResponse(
    @SerialName("type") val type: String,
)
