package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.CommentRoom

interface CommentRoomRepository {
    suspend fun fetchCommentRoom(memberId: Long): Result<List<CommentRoom>>
}
