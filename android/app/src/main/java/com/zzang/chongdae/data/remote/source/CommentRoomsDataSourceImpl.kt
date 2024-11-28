package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.source.CommentRoomsDataSource

class CommentRoomsDataSourceImpl(
    private val commentApiService: CommentApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomsResponse> {
        return runCatching {
            commentApiService.getCommentRooms(memberId).body() ?: throw IllegalStateException()
        }
    }
}
