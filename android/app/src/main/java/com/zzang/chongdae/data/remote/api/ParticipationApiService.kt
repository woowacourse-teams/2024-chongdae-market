package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.NicknameRequest
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ParticipationApiService {
    @POST("/participations")
    suspend fun postParticipations(
        @Body participationRequest: ParticipationRequest,
    ): Response<Unit>

    @GET("/participants")
    suspend fun getParticipants(
        @Query("offering-id") offeringId: Long,
    ): Response<ParticipantsResponse>

    @DELETE("/participations")
    suspend fun deleteParticipations(
        @Query("offering-id") offeringId: Long,
    ): Response<Unit>

    @PATCH("/member")
    suspend fun patchNickname(
        @Body request: NicknameRequest,
    ): Response<Unit>
}
