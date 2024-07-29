package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse

interface CommentRoomsDataSource {
    suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomsResponse>
}
