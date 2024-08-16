package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentOfferingInfoResponse(
    @SerialName("status")
    val status: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("buttonText")
    val buttonText: String,
    @SerialName("message")
    val message: String,
    @SerialName("title")
    val title: String,
    @SerialName("isProposer")
    val isProposer: Boolean,
)
