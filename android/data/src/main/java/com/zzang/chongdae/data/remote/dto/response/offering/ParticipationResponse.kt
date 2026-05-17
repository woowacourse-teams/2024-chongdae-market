package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipationResponse(
    @SerialName("offeringCondition") val offeringCondition: com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus,
    @SerialName("currentCount") val currentCount: Int,
)
