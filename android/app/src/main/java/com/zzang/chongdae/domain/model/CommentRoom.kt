package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class CommentRoom(
    val id: Long,
    val title: String,
    val latestComment: String,
    val latestCommentTime: LocalDateTime?,
    val isProposer: Boolean,
)
