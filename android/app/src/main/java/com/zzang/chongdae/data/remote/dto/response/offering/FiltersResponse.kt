package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiltersResponse(
    @SerialName("filters") val filters: List<RemoteFilter>,
)
