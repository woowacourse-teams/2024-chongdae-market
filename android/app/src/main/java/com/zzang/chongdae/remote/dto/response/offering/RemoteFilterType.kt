package com.zzang.chongdae.remote.dto.response.offering

import kotlinx.serialization.SerialName

enum class RemoteFilterType {
    @SerialName("VISIBLE")
    VISIBLE,

    @SerialName("INVISIBLE")
    INVISIBLE,
}
