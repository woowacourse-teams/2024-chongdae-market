package com.zzang.chongdae.remote.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    @SerialName("memberId") val memberId: Long,
    @SerialName("nickname") val nickname: String,
)
