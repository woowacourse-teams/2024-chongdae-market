package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.AuthApiService
import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.MemberResponse
import com.zzang.chongdae.data.remote.dto.response.SignupResponse
import com.zzang.chongdae.data.remote.dto.response.TokenResponse
import com.zzang.chongdae.data.source.AuthRemoteDataSource

class AuthRemoteDataSourceImpl(
    private val service: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun saveLogin(ciRequest: CiRequest): Result<TokenResponse> {
        return runCatching {
            val response = service.postLogin(ciRequest)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("${response.code()}")
            }
        }
    }

    override suspend fun saveSignup(ciRequest: CiRequest): Result<SignupResponse> {
        return runCatching {
            val response = service.postSignup(ciRequest)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("${response.code()}")
            }
        }
    }
}
