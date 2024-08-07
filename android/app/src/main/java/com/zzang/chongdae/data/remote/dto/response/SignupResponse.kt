package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    @SerialName("member") val memberResponse: MemberResponse,
    @SerialName("token") val tokenResponse: TokenResponse,
)