package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.CommentRoomListResponse

interface CommentRoomDataSource {
    suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomListResponse>
}
