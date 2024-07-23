package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse
import com.zzang.chongdae.data.remote.dto.response.ParticipationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GroupPurchaseApiService {
    @GET("/offerings")
    suspend fun getArticles(): Response<GroupPurchasesResponse>

    @POST("/participation")
    suspend fun postParticipation(
        @Body participationRequest: ParticipationRequest,
    ): Response<ParticipationResponse>
}
