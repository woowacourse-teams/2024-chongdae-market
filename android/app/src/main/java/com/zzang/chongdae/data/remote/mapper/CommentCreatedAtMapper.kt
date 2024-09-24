package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.comment.CommentCreatedAtResponse
import com.zzang.chongdae.domain.model.CommentCreatedAt

fun CommentCreatedAtResponse.toDomain(): CommentCreatedAt {
    return CommentCreatedAt(
        date = this.date.toLocalDate(),
        time = this.time.toLocalTime(),
    )
}
