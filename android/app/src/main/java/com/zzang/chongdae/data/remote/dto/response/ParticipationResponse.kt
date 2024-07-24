package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipationResponse(
    @SerialName("offeringCondition") val offeringCondition: RemoteOfferingCondition,
    @SerialName("currentCount") val currentCount: Int,
)
