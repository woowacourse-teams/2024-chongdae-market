package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentRoomsResponse
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsApiService {
    @GET("/comments")
    suspend fun getCommentRooms(
        @Query("member-id") memberId: Long,
    ): Response<CommentRoomsResponse>

    @GET("/comments/{offering-id}")
    suspend fun getComments(
        @Path("offering-id") offeringId: Long,
        @Query("member-id") memberId: Long,
    ): Response<CommentsResponse>

    @POST("/comments")
    suspend fun postComment(
        @Body commentRequest: CommentRequest,
    ): Response<Unit>
}
