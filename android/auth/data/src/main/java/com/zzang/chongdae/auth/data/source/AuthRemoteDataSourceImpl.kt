package com.zzang.chongdae.auth.data.source

import com.zzang.chongdae.auth.data.api.AuthApiService
import com.zzang.chongdae.auth.data.dto.request.TokensRequest
import com.zzang.chongdae.auth.data.dto.response.MemberResponse
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.network.safeApiCall
import javax.inject.Inject

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val service: AuthApiService,
    ) : AuthRemoteDataSource {
        override suspend fun saveLogin(tokensRequest: TokensRequest): Result<MemberResponse, DataError.Network> {
            return safeApiCall { service.postLogin(tokensRequest) }
        }

        override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
            return safeApiCall { service.postRefresh() }
        }
    }
