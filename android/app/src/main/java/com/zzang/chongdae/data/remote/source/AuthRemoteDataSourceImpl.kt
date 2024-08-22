package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.AuthApiService
import com.zzang.chongdae.data.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

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
