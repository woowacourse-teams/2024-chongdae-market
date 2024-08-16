package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class OfferingDetail(
    val id: Long,
    val title: String,
    val nickname: String,
    val isProposer: Boolean,
    val productUrl: String?,
    val thumbnailUrl: String?,
    val dividedPrice: Int,
    val totalPrice: Int,
    val meetingDate: LocalDateTime,
    val currentCount: CurrentCount,
    val totalCount: Int,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val description: String,
    val condition: OfferingCondition,
    val isParticipated: Boolean,
)

fun OfferingDetail.toOffering(): Offering {
    return Offering(
        id = this.id,
        title = this.title,
        meetingAddress = this.meetingAddress,
        thumbnailUrl = this.thumbnailUrl,
        totalCount = this.totalCount,
        currentCount = this.currentCount.value,
        dividedPrice = this.dividedPrice,
        originPrice = this.totalPrice / this.totalCount,
        status = this.condition,
        isOpen = !this.meetingDate.isBefore(LocalDateTime.now()),
    )
}
