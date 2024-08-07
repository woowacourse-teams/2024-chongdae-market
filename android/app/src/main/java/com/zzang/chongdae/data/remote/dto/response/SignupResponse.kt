package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    @SerialName("memberResponse") val memberResponse: MemberResponse,
    @SerialName("tokenResponse") val tokenResponse: TokenResponse,
)