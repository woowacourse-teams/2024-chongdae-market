package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface AuthRepository {
    suspend fun saveLogin(accessToken: String): Result<Member, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
