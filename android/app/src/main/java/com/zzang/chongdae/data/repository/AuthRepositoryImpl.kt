package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.data.source.AuthRemoteDataSource
import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun saveLogin(accessToken: String): Result<Member, DataError.Network> {
        return authRemoteDataSource.saveLogin(
            accessTokenRequest = AccessTokenRequest(accessToken),
        ).map { it.toDomain() }
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return when (val result = authRemoteDataSource.saveRefresh()) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.UNAUTHORIZED -> {
                        return Result.Error(result.msg, DataError.Network.FAIL_REFRESH)
                    }

                    else -> {
                        return Result.Error(result.msg, DataError.Network.UNAUTHORIZED)
                    }
                }
            }

            is Result.Success -> result
        }
    }
}
