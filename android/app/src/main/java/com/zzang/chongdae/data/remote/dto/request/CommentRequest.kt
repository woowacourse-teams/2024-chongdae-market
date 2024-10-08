package com.zzang.chongdae.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    @SerialName("offeringId") val offeringId: Long,
    @SerialName("content") val content: String,
)
