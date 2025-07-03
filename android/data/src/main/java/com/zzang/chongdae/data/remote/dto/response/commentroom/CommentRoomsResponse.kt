package com.zzang.chongdae.data.remote.dto.response.commentroom

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRoomsResponse(
    @SerialName("offerings") val commentRoom: List<com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomResponse>,
)
