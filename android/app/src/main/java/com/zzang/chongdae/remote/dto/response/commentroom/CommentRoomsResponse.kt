package com.zzang.chongdae.remote.dto.response.commentroom

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRoomsResponse(
    @SerialName("offerings") val commentRoom: List<CommentRoomResponse>,
)
