package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.remote.dto.response.comment.CommentResponse

fun CommentResponse.toDomain(): Comment {
    return Comment(
        content = this.content,
        commentCreatedAt = this.commentCreatedAtResponse.toDomain(),
        isMine = this.isMine,
        isProposer = this.isProposer,
        nickname = this.nickname,
    )
}
