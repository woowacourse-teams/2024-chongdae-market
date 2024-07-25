package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentDetailApiService {
    @GET("/offerings/{offering-id}/meetings")
    suspend fun getMeetings(
        @Path("offering-id") offeringId: Long,
    ): Response<MeetingsResponse>

    @POST("/comments")
    suspend fun postComment(
        @Body commentRequest: CommentRequest,
    ): Response<Unit>

    @GET("/comments/{offering-id}")
    suspend fun getComments(
        @Path("offering-id") offeringId: Long,
        @Query("member-id") memberId: Long,
    ): Response<CommentsResponse>
}
