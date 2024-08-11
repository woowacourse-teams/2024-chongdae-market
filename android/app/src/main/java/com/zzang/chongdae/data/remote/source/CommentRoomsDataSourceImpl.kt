package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.source.CommentRoomsDataSource

class CommentRoomsDataSourceImpl(
    private val commentApiService: CommentApiService,
) : CommentRoomsDataSource {
    override suspend fun fetchCommentRooms(): Result<CommentRoomsResponse> {
        return runCatching {
            val response = commentApiService.getCommentRooms()
            if(response.isSuccessful){
                response.body() ?: error("에러 발생: null")
            } else {
                error("${response.code()}")
            }
        }
    }
}
