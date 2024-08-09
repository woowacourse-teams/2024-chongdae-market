package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRoomsResponse(
    @SerialName("offerings") val commentRoom: List<CommentRoomResponse>,
)
