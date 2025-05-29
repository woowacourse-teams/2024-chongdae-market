package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.response.analytics.UserTypeResponse
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsApiService {
    @GET("/analytics/variant")
    suspend fun getUserType(): Response<UserTypeResponse>
}
