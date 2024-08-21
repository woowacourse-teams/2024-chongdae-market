package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface CommentRoomsRepository {
    suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network>
}
