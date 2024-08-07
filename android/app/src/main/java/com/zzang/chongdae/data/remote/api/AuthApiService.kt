package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.SignupResponse
import com.zzang.chongdae.data.remote.dto.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login")
    suspend fun postLogin(
        @Body ci: CiRequest,
    ): Response<TokenResponse>

    @POST("/auth/refresh")
    suspend fun postRefresh(
        @Body refreshToken: String,
    ): Response<Unit>

    @POST("/auth/signup")
    suspend fun postSignup(
        @Body ci: CiRequest,
    ): Response<SignupResponse>
}
