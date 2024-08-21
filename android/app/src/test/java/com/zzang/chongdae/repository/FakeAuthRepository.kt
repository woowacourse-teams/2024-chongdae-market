package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class FakeAuthRepository : AuthRepository {
    override suspend fun saveLogin(ci: String): Result<Member, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRefresh(): Result<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSignup(ci: String): Result<Member, DataError.Network> {
        TODO("Not yet implemented")
    }
}
