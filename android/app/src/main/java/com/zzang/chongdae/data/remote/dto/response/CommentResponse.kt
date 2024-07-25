package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable()
data class CommentResponse(
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val commentCreatedAtResponse: CommentCreatedAtResponse,
    @SerialName("isMine")
    val isMine: Boolean,
    @SerialName("isProposer")
    val isProposer: Boolean,
    @SerialName("nickname")
    val nickname: String,
)
