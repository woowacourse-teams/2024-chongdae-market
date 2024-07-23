package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class OfferingDetail(
    val id: Long,
    val title: String,
    val nickname: String,
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
    val status: OfferingCondition,
)

/*
@Serializable
data class OfferingDetailResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("memberId") val memberId: Long,
    @SerialName("productUrl") val productUrl: String,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("meetingAddressDetail") val meetingAddressDetail: String,
    @SerialName("description") val description: String,
    @SerialName("deadline") val deadline: String,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("totalPrice") val totalPrice: Int,
    @SerialName("condition") val status: OfferingCondition,
)*/
