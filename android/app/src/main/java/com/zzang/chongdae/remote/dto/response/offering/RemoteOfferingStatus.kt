package com.zzang.chongdae.remote.dto.response.offering

import kotlinx.serialization.SerialName

enum class RemoteOfferingStatus {
    @SerialName("FULL")
    FULL,

    @SerialName("IMMINENT")
    IMMINENT,

    @SerialName("CONFIRMED")
    CONFIRMED,

    @SerialName("AVAILABLE")
    AVAILABLE,
}
