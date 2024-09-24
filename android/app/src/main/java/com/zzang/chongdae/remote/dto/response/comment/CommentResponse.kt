package com.zzang.chongdae.remote.dto.response.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName("commentId")
    val commentId: Long,
    @SerialName("createdAt")
    val commentCreatedAtResponse: CommentCreatedAtResponse,
    @SerialName("content")
    val content: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("isProposer")
    val isProposer: Boolean,
    @SerialName("isMine")
    val isMine: Boolean,
)
