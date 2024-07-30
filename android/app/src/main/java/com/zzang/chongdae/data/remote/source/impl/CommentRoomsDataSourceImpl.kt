package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.CommentsApiService
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSource

class CommentRoomsDataSourceImpl(
    private val commentsApiService: CommentsApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(memberId: Long): Result<CommentRoomsResponse> {
        return runCatching {
            commentsApiService.getCommentRooms(memberId).body() ?: throw IllegalStateException()
        }
    }
}
