package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.api.AuthApiService
import com.zzang.chongdae.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.remote.dto.response.auth.MemberResponse
import com.zzang.chongdae.remote.util.safeApiCall

class AuthRemoteDataSourceImpl(
    private val service: com.zzang.chongdae.remote.api.AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun saveLogin(accessTokenRequest: AccessTokenRequest): Result<MemberResponse, DataError.Network> {
        return safeApiCall { service.postLogin(accessTokenRequest) }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return safeApiCall { service.postRefresh() }
    }
}
