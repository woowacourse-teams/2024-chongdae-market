package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFilter(
    @SerialName("name") val name: RemoteFilterName,
    @SerialName("value") val value: String,
    @SerialName("type") val type: RemoteFilterType,
)
