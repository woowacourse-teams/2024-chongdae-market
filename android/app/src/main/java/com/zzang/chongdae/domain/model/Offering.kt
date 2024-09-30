package com.zzang.chongdae.domain.model

data class Offering(
    val id: Long,
    val title: String,
    val meetingAddressDong: String,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val currentCount: Int,
    val dividedPrice: Int,
    val originPrice: Int?,
    val status: OfferingCondition,
    val isOpen: Boolean,
) {
    fun calculateDiscountRate(): Float? {
        if (originPrice == null) return null
        val discountPrice = originPrice - dividedPrice
        return (discountPrice.toFloat() / originPrice) * 100
    }
}
