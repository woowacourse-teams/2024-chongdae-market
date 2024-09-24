package com.zzang.chongdae.remote.dto.response.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsResponse(
    @SerialName("comments")
    val commentsResponse: List<CommentResponse>,
)
