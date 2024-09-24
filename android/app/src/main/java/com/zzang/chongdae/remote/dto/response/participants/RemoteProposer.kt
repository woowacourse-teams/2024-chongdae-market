package com.zzang.chongdae.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProposer(
    @SerialName("nickname")
    val nickname: String,
)
