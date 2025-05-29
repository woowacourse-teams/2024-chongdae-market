package com.zzang.chongdae.data.remote.mapper.comment

import com.zzang.chongdae.data.remote.dto.response.comment.CommentCreatedAtResponse
import com.zzang.chongdae.data.remote.mapper.toLocalDate
import com.zzang.chongdae.data.remote.mapper.toLocalTime
import com.zzang.chongdae.domain.model.comment.CommentCreatedAt

fun CommentCreatedAtResponse.toDomain(): CommentCreatedAt {
    return CommentCreatedAt(
        date = this.date.toLocalDate(),
        time = this.time.toLocalTime(),
    )
}
