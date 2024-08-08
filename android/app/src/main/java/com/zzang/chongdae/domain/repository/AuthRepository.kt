package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Member

interface AuthRepository {
    suspend fun saveLogin(ci: String): Result<Unit>

    suspend fun saveRefresh(): Result<Unit>

    suspend fun saveSignup(ci: String): Result<Member>
}
