package com.zzang.chongdae.domain.model

data class Offering(
    val id: Long,
    val title: String,
    val meetingAddress: String,
    val thumbnailUrl: String?,
    val totalCount: Int,
    val currentCount: Int,
    val dividedPrice: Int,
    val eachPrice: Int?,
    val status: OfferingCondition,
    val isOpen: Boolean,
) {
    fun calculateDiscountRate(): Float? {
        if (eachPrice == null) return null
        val discountPrice = eachPrice - dividedPrice
        return (discountPrice.toFloat() / eachPrice) * 100
    }
}
