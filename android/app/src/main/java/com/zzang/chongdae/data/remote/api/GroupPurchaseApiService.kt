package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse
import retrofit2.Response
import retrofit2.http.GET

interface GroupPurchaseApiService {
    @GET("/offerings")
    suspend fun getArticles(): Response<GroupPurchasesResponse>
}
