package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.CommentRoomResponse
import com.zzang.chongdae.domain.model.CommentRoom
import java.time.LocalDateTime

fun CommentRoomResponse.toDomain(): CommentRoom {
    return CommentRoom(
        id = this.offeringId,
        title = this.offeringTitle,
        latestComment = this.latestComment.content ?: "",
        latestCommentTime = this.latestComment.createdAt?.toLocalDateTime(),
        isProposer = this.isProposer,
    )
}
