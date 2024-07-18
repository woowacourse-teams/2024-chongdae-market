package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class ArticleDetail(
    val id: Long = 0,
    val title: String,
    val nickname: String,
    val productUrl: String,
    val thumbnailUrl:String,
    val splitPrice: Int,
    val totalPrice: Int,
    val dueDateTime: LocalDateTime,
    val currentCount: CurrentCount,
    val totalCount: Int,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val description: String,
    val status: ArticleStatus,
)
