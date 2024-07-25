package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentCreatedAtResponse(
    @SerialName("date")
    val date: String,
    @SerialName("time")
    val time: String,
)
