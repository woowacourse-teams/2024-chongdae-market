package com.zzang.chongdae.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipationRequest(
    @SerialName("offeringId") val offeringId: Long,
)
