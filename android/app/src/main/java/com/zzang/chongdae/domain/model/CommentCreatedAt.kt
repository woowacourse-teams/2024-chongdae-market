package com.zzang.chongdae.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class CommentCreatedAt(
    val date: LocalDate,
    val time: LocalTime,
)
