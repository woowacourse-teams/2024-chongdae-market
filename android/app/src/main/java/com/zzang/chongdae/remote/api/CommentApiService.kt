package com.zzang.chongdae.remote.api

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.data.remote.dto.response.comment.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.comment.UpdatedStatusResponse
import com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

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

    @GET("/comments/info")
    suspend fun getCommentOfferingInfo(
        @Query("offering-id") offeringId: Long,
    ): Response<CommentOfferingInfoResponse>

    @PATCH("/comments/status")
    suspend fun patchOfferingStatus(
        @Query("offering-id") offeringId: Long,
    ): Response<UpdatedStatusResponse>
}
