package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OfferingDetailApiService {
    @GET("/offerings/{id}")
    suspend fun getOfferingDetail(
        @Path("id") id: Long,
    ): Response<OfferingDetailResponse>
}
