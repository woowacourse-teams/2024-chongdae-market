package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login/kakao")
    suspend fun postLogin(
        @Body accessToken: AccessTokenRequest,
    ): Response<MemberResponse>

    @POST("/auth/refresh")
    suspend fun postRefresh(): Response<Unit>
}
