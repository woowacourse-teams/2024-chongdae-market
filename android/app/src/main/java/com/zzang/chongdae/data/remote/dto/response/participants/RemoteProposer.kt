package com.zzang.chongdae.data.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProposer(
    @SerialName("nickname")
    val nickname: String,
)
