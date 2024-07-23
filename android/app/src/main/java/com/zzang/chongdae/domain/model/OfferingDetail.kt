package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class OfferingDetail(
    val id: Long,
    val title: String,
    val nickname: String,
    val memberId: String,
    val productUrl: String,
    val thumbnailUrl: String,
    val splitPrice: Int,
    val totalPrice: Int,
    val dueDateTime: LocalDateTime,
    val currentCount: CurrentCount,
    val totalCount: Int,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val description: String,
    val condition: OfferingCondition,
)

