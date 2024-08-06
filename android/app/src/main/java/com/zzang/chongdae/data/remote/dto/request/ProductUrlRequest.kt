package com.zzang.chongdae.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUrlRequest(
    @SerialName("productUrl") val productUrl: String,
)
