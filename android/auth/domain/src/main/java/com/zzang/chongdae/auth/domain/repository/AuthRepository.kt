package com.zzang.chongdae.auth.domain.repository

import com.zzang.chongdae.auth.domain.model.Member
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

interface AuthRepository {
    suspend fun postLogin(
        accessToken: String,
        fcmToken: String,
    ): Result<Member, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
