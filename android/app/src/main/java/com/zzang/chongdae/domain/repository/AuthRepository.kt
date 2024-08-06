package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.mo.Member

interface AuthRepository {
    suspend fun saveLogin(ci: String): Result<Unit>

    suspend fun saveSignup(ci: String): Result<Member>
}
