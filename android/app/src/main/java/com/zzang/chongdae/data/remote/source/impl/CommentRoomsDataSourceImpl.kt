package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.CommentRoomsApiService
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSource

class CommentRoomsDataSourceImpl(
    private val service: CommentRoomsApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomsResponse> {
        return runCatching {
            service.getCommentRooms(memberId).body() ?: throw IllegalStateException()
        }
    }
}
