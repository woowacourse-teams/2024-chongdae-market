package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ParticipationsApiService {
    @POST("/participations")
    suspend fun postParticipations(
        @Body participationRequest: ParticipationRequest,
    ): Response<Unit>
}
