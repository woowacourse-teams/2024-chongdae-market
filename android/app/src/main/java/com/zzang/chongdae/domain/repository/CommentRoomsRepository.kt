package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.CommentRoom

interface CommentRoomsRepository {
    suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network>
}
