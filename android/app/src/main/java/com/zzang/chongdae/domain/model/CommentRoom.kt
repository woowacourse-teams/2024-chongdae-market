package com.zzang.chongdae.domain.model

import java.time.LocalTime

data class CommentRoom(
    val title: String,
    val description: String,
    val lastCommentTime: LocalTime,
)