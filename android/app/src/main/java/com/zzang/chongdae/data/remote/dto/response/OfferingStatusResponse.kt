package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferingStatusResponse(
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("status")
    val status: String,
    @SerialName("buttonText")
    val buttonText: String,
)
