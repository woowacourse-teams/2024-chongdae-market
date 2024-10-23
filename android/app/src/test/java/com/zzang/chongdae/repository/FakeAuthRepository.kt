package com.zzang.chongdae.repository

import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

class FakeAuthRepository : AuthRepository {
    override suspend fun saveLogin(
        accessToken: String,
        fcmToken: String,
    ): Result<Member, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }
}
