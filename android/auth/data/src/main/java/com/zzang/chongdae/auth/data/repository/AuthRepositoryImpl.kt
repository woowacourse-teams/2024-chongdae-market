package com.zzang.chongdae.auth.data.repository

import com.zzang.chongdae.auth.data.dto.request.TokensRequest
import com.zzang.chongdae.auth.data.mapper.toDomain
import com.zzang.chongdae.auth.data.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.domain.model.Member
import com.zzang.chongdae.auth.domain.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authRemoteDataSource: AuthRemoteDataSource,
    ) : AuthRepository {
        override suspend fun postLogin(
            accessToken: String,
            fcmToken: String,
        ): Result<Member, DataError.Network> {
            return authRemoteDataSource.saveLogin(
                tokensRequest = TokensRequest(accessToken, fcmToken),
            ).map { it.toDomain() }
        }

        override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
            return authRemoteDataSource.saveRefresh()
        }
    }
