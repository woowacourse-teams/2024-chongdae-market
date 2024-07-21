package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class Article(
    val id: Long,
    val thumbnailUrl: String,
    val deadline: LocalDateTime,
    val title: String,
    val meetingAddress: String,
    val splitPrice: Int,
    val totalCount: Int,
    val currentCount: Int,
    val isClosed: Boolean,
)
