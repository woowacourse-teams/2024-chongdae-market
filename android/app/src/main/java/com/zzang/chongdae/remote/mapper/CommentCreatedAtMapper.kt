package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.CommentCreatedAt
import com.zzang.chongdae.remote.dto.response.comment.CommentCreatedAtResponse

fun CommentCreatedAtResponse.toDomain(): CommentCreatedAt {
    return CommentCreatedAt(
        date = this.date.toLocalDate(),
        time = this.time.toLocalTime(),
    )
}
