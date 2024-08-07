package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.MemberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login")
    suspend fun postLogin(
        @Body ci: CiRequest,
    ): Response<Unit>

    @POST("/auth/refresh")
    suspend fun postRefresh(): Response<Unit>

    @POST("/auth/signup")
    suspend fun postSignup(
        @Body ci: CiRequest,
    ): Response<MemberResponse>
}
