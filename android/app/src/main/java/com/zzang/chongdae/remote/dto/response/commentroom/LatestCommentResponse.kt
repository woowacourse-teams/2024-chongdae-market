package com.zzang.chongdae.remote.dto.response.commentroom

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestCommentResponse(
    @SerialName("content") val content: String?,
    @SerialName("createdAt") val createdAt: String?,
)
