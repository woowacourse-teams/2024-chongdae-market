package com.zzang.chongdae.auth.api

import com.zzang.chongdae.auth.dto.request.TokenRequest
import com.zzang.chongdae.auth.dto.response.MemberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login/kakao")
    suspend fun postLogin(
        @Body tokenRequest: TokenRequest,
    ): Response<MemberResponse>

    @POST("/auth/refresh")
    suspend fun postRefresh(): Response<Unit>
}
