package com.zzang.chongdae.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRoomResponse(
    @SerialName("offeringId") val offeringId: Long,
    @SerialName("offeringTitle") val offeringTitle: String,
    @SerialName("latestComment") val latestComment: LatestCommentResponse,
    @SerialName("isProposer") val isProposer: Boolean,
)
