package com.zzang.chongdae.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    @SerialName("accessToken") val accessToken: String,
)
