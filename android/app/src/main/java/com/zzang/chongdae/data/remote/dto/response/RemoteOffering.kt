package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOffering(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("meetingAddress") val meetingAddress: String,
    @SerialName("currentCount") val currentCount: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("dividedPrice") val dividedPrice: Int,
    @SerialName("condition") val condition: RemoteOfferingCondition,
    @SerialName("isOpen") val isOpen: Boolean,
)
