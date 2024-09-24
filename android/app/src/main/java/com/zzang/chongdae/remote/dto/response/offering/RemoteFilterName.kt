package com.zzang.chongdae.remote.dto.response.offering

import kotlinx.serialization.SerialName

enum class RemoteFilterName {
    @SerialName("JOINABLE")
    JOINABLE,

    @SerialName("IMMINENT")
    IMMINENT,

    @SerialName("HIGH_DISCOUNT")
    HIGH_DISCOUNT,

    @SerialName("RECENT")
    RECENT,
}
