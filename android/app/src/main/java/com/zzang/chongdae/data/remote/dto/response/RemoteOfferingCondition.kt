package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName

enum class RemoteOfferingCondition {
    @SerialName("FULL")
    FULL,

    @SerialName("IMMINENT")
    IMMINENT,

    @SerialName("CONFIRMED")
    CONFIRMED,

    @SerialName("AVAILABLE")
    AVAILABLE,
}
