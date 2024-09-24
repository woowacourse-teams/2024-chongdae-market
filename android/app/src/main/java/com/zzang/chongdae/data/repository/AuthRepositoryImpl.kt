package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.remote.mapper.toDomain

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
