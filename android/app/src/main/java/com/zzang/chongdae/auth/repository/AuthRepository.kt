package com.zzang.chongdae.auth.repository

import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

interface AuthRepository {
    suspend fun saveLogin(accessToken: String): Result<Member, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}