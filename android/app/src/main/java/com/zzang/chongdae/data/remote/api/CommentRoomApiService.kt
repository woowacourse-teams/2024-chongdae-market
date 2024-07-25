package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.CommentRoomListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentRoomApiService {
    @GET("/comments")
    suspend fun getCommentRoom(
        @Query("member-id") memberId: Long,
    ): Response<CommentRoomListResponse>
}
