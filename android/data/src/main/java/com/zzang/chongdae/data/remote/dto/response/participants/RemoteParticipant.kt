package com.zzang.chongdae.data.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteParticipant(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("count")
    val count: Int,
    @SerialName("price")
    val price: Int,
)
