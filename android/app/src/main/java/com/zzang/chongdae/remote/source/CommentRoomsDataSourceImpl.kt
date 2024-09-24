package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.api.CommentApiService
import com.zzang.chongdae.remote.dto.response.commentroom.CommentRoomsResponse
import com.zzang.chongdae.remote.util.safeApiCall

class CommentRoomsDataSourceImpl(
    private val commentApiService: CommentApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(): Result<CommentRoomsResponse, DataError.Network> {
        return safeApiCall { commentApiService.getCommentRooms() }
    }
}
