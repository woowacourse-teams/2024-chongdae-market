package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFilter(
    @SerialName("name") val name: com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName,
    @SerialName("value") val value: String,
    @SerialName("type") val type: com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterType,
)
