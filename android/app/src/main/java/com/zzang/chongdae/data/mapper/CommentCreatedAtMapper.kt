package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.CommentCreatedAtResponse
import com.zzang.chongdae.domain.model.CommentCreatedAt

fun CommentCreatedAtResponse.toDomain(): CommentCreatedAt {
    return CommentCreatedAt(
        date = this.date.toLocalDate(),
        time = this.time.toLocalTime(),
    )
}
