package com.zzang.chongdae.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipationRequest(
    @SerialName("memberId") val memberId: Long,
    @SerialName("offeringId") val offeringId: Long,
)
