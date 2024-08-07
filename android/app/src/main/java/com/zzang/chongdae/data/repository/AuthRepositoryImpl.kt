package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.model.Signup
import com.zzang.chongdae.domain.model.Token
import com.zzang.chongdae.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun saveLogin(ci: String): Result<Token> {
        return authRemoteDataSource.saveLogin(
            ciRequest = CiRequest(ci),
        ).mapCatching { it.toDomain()}
    }

    override suspend fun saveSignup(ci: String): Result<Signup> {
        return authRemoteDataSource.saveSignup(
            ciRequest = CiRequest(ci),
        ).mapCatching { it.toDomain() }
    }
}
