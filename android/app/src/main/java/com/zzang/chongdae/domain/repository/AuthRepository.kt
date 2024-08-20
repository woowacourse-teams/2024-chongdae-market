package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface AuthRepository {
    suspend fun saveLogin(ci: String): Result<Member, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>

    suspend fun saveSignup(ci: String): Result<Member, DataError.Network>
}
