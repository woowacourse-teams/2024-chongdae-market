package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApiService {
    @GET("/comments")
    suspend fun getCommentRooms(): Response<CommentRoomsResponse>

    @GET("/comments/messages")
    suspend fun getComments(
        @Query("offering-id") offeringId: Long,
    ): Response<CommentsResponse>

    @POST("/comments")
    suspend fun postComment(
        @Body commentRequest: CommentRequest,
    ): Response<Unit>
}
