package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.CommentRoomListResponse
import retrofit2.Response
import retrofit2.http.GET

interface CommentRoomApiService {
    @GET("/comments")
    suspend fun getCommentRoom(): Response<CommentRoomListResponse>
}