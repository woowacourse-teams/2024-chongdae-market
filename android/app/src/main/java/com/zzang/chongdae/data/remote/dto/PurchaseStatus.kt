package com.zzang.chongdae.data.remote.dto

import kotlinx.serialization.SerialName

enum class PurchaseStatus {
    @SerialName("FULL")
    FULL,

    @SerialName("TIME_OUT")
    TIME_OUT,

    @SerialName("CONFIRMED")
    CONFIRMED,

    @SerialName("AVAILABLE")
    AVAILABLE,
}
