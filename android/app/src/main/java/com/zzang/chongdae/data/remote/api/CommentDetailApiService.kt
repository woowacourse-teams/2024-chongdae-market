package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentDetailApiService {
    @GET("/offerings/{offering-id}/meetings")
    suspend fun getMeetings(
        @Path("offering-id") offeringId: Long,
    ): Response<MeetingsResponse>
}
