package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.AuthApiService
import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class AuthRemoteDataSourceImpl(
    private val service: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun saveLogin(ciRequest: CiRequest): Result<MemberResponse, DataError.Network> {
        return safeApiCall { service.postLogin(ciRequest) }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return safeApiCall { service.postRefresh() }
    }

    override suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse, DataError.Network> {
        return safeApiCall { service.postSignup(ciRequest) }
    }
}
