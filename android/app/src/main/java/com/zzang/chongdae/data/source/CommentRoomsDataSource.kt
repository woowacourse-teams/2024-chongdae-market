package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse

interface CommentRoomsDataSource {
    suspend fun fetchCommentRooms(): Result<CommentRoomsResponse>
}
