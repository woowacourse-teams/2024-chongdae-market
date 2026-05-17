package com.zzang.chongdae.repository

import com.zzang.chongdae.auth.data.model.Member
import com.zzang.chongdae.auth.data.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

class FakeAuthRepository : com.zzang.chongdae.auth.data.repository.AuthRepository {
    override suspend fun postLogin(
        accessToken: String,
        fcmToken: String,
    ): Result<com.zzang.chongdae.auth.data.model.Member, DataError.Network> {
        return Result.Success(com.zzang.chongdae.auth.data.model.Member(0, "dummy"))
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }
}
