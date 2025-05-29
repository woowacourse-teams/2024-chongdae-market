package com.zzang.chongdae.domain.model.comment

import java.time.LocalDate
import java.time.LocalTime

data class CommentCreatedAt(
    val date: LocalDate,
    val time: LocalTime,
)
