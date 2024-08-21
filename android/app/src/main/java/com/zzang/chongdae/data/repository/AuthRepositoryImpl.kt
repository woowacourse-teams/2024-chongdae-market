package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun saveLogin(ci: String): Result<Member, DataError.Network> {
        return authRemoteDataSource.saveLogin(
            ciRequest = CiRequest(ci),
        ).map { it.toDomain() }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return authRemoteDataSource.saveRefresh()
    }

    override suspend fun saveSignup(ci: String): Result<Member, DataError.Network> {
        return authRemoteDataSource.saveSignup(
            ciRequest = CiRequest(ci),
        ).map { it.toDomain() }
    }
}
