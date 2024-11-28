package com.zzang.chongdae.domain.model

data class Offering(
    val id: Long,
    val title: String,
    val meetingAddress: String,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val currentCount: Int,
    val dividedPrice: Int,
    val condition: OfferingCondition,
    val isOpen: Boolean,
)
