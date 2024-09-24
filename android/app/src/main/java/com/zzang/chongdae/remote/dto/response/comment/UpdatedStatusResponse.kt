package com.zzang.chongdae.remote.dto.response.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedStatusResponse(
    @SerialName("updatedStatus")
    val updatedStatus: String,
)
