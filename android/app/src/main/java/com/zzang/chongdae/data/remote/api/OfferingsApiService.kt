package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OfferingsApiService {
    @GET("/offerings")
    suspend fun getArticles(
        @Query("last-id") lastOfferingId: Long,
        @Query("page-size") pageSize: Int,
    ): Response<OfferingsResponse>
}
