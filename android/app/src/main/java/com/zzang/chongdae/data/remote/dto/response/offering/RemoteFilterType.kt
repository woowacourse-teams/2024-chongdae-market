package com.zzang.chongdae.data.remote.dto.response.offering

import kotlinx.serialization.SerialName

enum class RemoteFilterType {
    @SerialName("VISIBLE")
    VISIBLE,

    @SerialName("INVISIBLE")
    INVISIBLE,
}
