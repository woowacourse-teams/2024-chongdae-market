package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.local.model.CommentEntity
import com.zzang.chongdae.data.remote.dto.response.CommentResponse
import com.zzang.chongdae.domain.model.Comment

fun CommentResponse.toDomain(): Comment {
    return Comment(
        content = this.content,
        commentCreatedAt = this.commentCreatedAtResponse.toDomain(),
        isMine = this.isMine,
        isProposer = this.isProposer,
        nickname = this.nickname,
    )
}

fun mapToCommentEntity(
    offeringId: Long,
    commentResponse: CommentResponse,
): CommentEntity {
    return CommentEntity(
        offeringId = offeringId,
        commentId = commentResponse.commentId,
        content = commentResponse.content,
        isMine = commentResponse.isMine,
        isProposer = commentResponse.isProposer,
        nickname = commentResponse.nickname,
        commentCreatedAtDate = commentResponse.commentCreatedAtResponse.date,
        commentCreatedAtTime = commentResponse.commentCreatedAtResponse.time,
    )
}
