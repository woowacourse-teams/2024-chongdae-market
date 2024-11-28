package com.zzang.chongdae.auth.repository

import com.zzang.chongdae.auth.dto.request.TokensRequest
import com.zzang.chongdae.auth.mapper.toDomain
import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthDataSourceQualifier
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        @AuthDataSourceQualifier private val authRemoteDataSource: AuthRemoteDataSource,
    ) : AuthRepository {
        override suspend fun saveLogin(
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
