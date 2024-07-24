package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestCommentResponse(
    @SerialName("content") val content: String,
    @SerialName("createdAt") val createdAt: String,
)
