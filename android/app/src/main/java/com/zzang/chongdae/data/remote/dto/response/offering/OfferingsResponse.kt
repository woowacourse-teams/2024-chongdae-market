package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferingsResponse(
    @SerialName("offerings") val offerings: List<RemoteOffering>,
)
