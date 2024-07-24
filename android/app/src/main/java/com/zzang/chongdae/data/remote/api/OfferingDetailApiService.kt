package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.dto.response.ParticipationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferingDetailApiService {
    @GET("/offerings/{offering-id}")
    suspend fun getOfferingDetail(
        @Path("offering-id") offeringId: Long,
        @Query("member-id") memberId: Long,
    ): Response<OfferingDetailResponse>

    @POST("/participations")
    suspend fun postParticipations(
        @Body participationRequest: ParticipationRequest,
    ): Response<ParticipationResponse>
}
