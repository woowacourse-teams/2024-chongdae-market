package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class OfferingModifyDomainResponse(
    val id: Long,
    val title: String,
    val productUrl: String?,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val description: String,
    val meetingDate: LocalDateTime,
    val currentCount: CurrentCount,
    val totalCount: Int,
    val thumbnailUrl: String?,
    val dividedPrice: Int,
    val totalPrice: Int,
    val condition: OfferingCondition,
    val nickname: String,
)
