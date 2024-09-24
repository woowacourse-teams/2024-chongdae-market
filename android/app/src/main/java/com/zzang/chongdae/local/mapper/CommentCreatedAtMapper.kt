package com.zzang.chongdae.local.mapper

import com.zzang.chongdae.domain.model.CommentCreatedAt
import com.zzang.chongdae.remote.dto.response.comment.CommentCreatedAtResponse
import com.zzang.chongdae.remote.mapper.toLocalDate
import com.zzang.chongdae.remote.mapper.toLocalTime

fun CommentCreatedAtResponse.toDomain(): CommentCreatedAt {
    return CommentCreatedAt(
        date = this.date.toLocalDate(),
        time = this.time.toLocalTime(),
    )
}
