package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomResponse
import com.zzang.chongdae.domain.model.commentroom.CommentRoom

fun com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomResponse.toDomain(): CommentRoom {
    return CommentRoom(
        id = this.offeringId,
        title = this.offeringTitle,
        latestComment = this.latestComment.content ?: "",
        latestCommentTime = this.latestComment.createdAt?.toLocalDateTime(),
        isProposer = this.isProposer,
    )
}
