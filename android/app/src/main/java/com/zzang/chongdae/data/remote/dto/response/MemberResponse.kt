package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    @SerialName("nickname") val nickname: String,
    @SerialName("memberId") val memberId: Long,
)
