package com.zzang.chongdae.repository

import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

class FakeAuthRepository : AuthRepository {
    override suspend fun postLogin(
        accessToken: String,
        fcmToken: String,
    ): Result<Member, DataError.Network> {
        return Result.Success(Member(0, "dummy"))
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }
}
