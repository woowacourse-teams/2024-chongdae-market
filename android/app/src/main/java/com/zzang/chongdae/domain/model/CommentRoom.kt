package com.zzang.chongdae.domain.model

import java.time.LocalTime

data class CommentRoom(
    val id: Long,
    val title: String,
    val description: String,
    val lastCommentTime: LocalTime,
)
