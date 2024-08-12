package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {
    override suspend fun saveLogin(ci: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRefresh(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSignup(ci: String): Result<Member> {
        TODO("Not yet implemented")
    }
}
