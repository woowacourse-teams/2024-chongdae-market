package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentRoomsApiService {
    @GET("/comments")
    suspend fun getCommentRooms(
        @Query("member-id") memberId: Long,
    ): Response<CommentRoomsResponse>
}
