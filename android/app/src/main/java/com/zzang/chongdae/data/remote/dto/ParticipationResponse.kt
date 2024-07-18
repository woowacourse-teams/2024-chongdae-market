package com.zzang.chongdae.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParticipationResponse(
    val currentCount: Int,
    val status: PurchaseStatus,
)
