package com.zzang.chongdae.auth.source

import com.zzang.chongdae.auth.dto.request.TokenRequest
import com.zzang.chongdae.auth.dto.response.MemberResponse
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

interface AuthRemoteDataSource {
    suspend fun saveLogin(tokenRequest: TokenRequest): Result<MemberResponse, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
