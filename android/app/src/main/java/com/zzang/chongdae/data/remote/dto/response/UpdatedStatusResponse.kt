package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedStatusResponse(
    @SerialName("updatedStatus")
    val updatedStatus: String,
)
