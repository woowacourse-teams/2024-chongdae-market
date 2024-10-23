package com.zzang.chongdae.auth.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokensRequest(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("fcmToken") val fcmToken: String,
)
