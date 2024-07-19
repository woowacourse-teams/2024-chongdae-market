package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.GroupPurchaseDetailResponse
import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse
import com.zzang.chongdae.data.remote.dto.response.ParticipationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupPurchaseApiService {
    @GET("/articles")
    suspend fun getArticles(): Response<GroupPurchasesResponse>

    @GET("/articles/{id}")
    suspend fun getArticleDetail(
        @Path("id") id: Long,
    ): Response<GroupPurchaseDetailResponse>

    @POST("/participation")
    suspend fun postParticipation(
        @Body participationRequest: ParticipationRequest,
    ): Response<ParticipationResponse>
}
