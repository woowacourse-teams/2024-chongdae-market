package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.CommentRoomApiService
import com.zzang.chongdae.data.remote.dto.response.CommentRoomListResponse
import com.zzang.chongdae.data.remote.source.CommentRoomDataSource

class CommentRoomDataSourceImpl(
    private val service: CommentRoomApiService,
) : CommentRoomDataSource {
    override suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomListResponse> {
        return runCatching {
            service.getCommentRoom(memberId).body() ?: throw IllegalStateException()
        }
    }
}
