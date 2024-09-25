package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomsResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.CommentRoomsDataSource

class CommentRoomsDataSourceImpl(
    private val commentApiService: com.zzang.chongdae.data.remote.api.CommentApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(): Result<CommentRoomsResponse, DataError.Network> {
        return safeApiCall { commentApiService.getCommentRooms() }
    }
}
