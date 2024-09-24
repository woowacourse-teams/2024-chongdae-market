package com.zzang.chongdae.remote.dto.response.offering

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUrlResponse(
    @SerialName("imageUrl")
    val imageUrl: String,
)
