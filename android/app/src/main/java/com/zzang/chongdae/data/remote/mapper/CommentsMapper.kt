package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.comment.CommentResponse
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
