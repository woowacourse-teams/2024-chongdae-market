package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.AuthApiService
import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.MemberResponse
import com.zzang.chongdae.data.source.AuthRemoteDataSource

class AuthRemoteDataSourceImpl(
    private val service: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun saveLogin(ci: String): Result<Unit> {
        return runCatching {
            service.postLogin(ci).body() ?: throw IllegalStateException()
        }
    }

    override suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse> {
        return runCatching {
            service.postSignup(ciRequest).body() ?: throw IllegalStateException()
        }
    }
}
