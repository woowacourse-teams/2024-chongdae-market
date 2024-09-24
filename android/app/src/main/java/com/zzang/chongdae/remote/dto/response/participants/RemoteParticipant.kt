package com.zzang.chongdae.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteParticipant(
    @SerialName("nickname")
    val nickname: String,
)
