package com.zzang.chongdae.auth.source

import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.auth.dto.request.AccessTokenRequest
import com.zzang.chongdae.auth.dto.response.MemberResponse
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.util.safeApiCall

class AuthRemoteDataSourceImpl(
    private val service: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun saveLogin(accessTokenRequest: AccessTokenRequest): Result<MemberResponse, DataError.Network> {
        return safeApiCall { service.postLogin(accessTokenRequest) }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return safeApiCall { service.postRefresh() }
    }
}
