package com.zzang.chongdae.auth.repository

import com.zzang.chongdae.auth.dto.request.AccessTokenRequest
import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.auth.mapper.toDomain
import com.zzang.chongdae.auth.source.AuthRemoteDataSource

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun saveLogin(accessToken: String): Result<Member, DataError.Network> {
        return authRemoteDataSource.saveLogin(
            accessTokenRequest = AccessTokenRequest(accessToken),
        ).map { it.toDomain() }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return authRemoteDataSource.saveRefresh()
    }
}
