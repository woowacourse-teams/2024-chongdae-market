package com.zzang.chongdae.domain.model.comment

data class Comment(
    val content: String,
    val commentCreatedAt: CommentCreatedAt,
    val isMine: Boolean,
    val isProposer: Boolean,
    val nickname: String,
)
