package com.zzang.chongdae.auth.source

import com.zzang.chongdae.auth.dto.request.AccessTokenRequest
import com.zzang.chongdae.auth.dto.response.MemberResponse
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface AuthRemoteDataSource {
    suspend fun saveLogin(accessTokenRequest: AccessTokenRequest): Result<MemberResponse, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
