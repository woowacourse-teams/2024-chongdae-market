package com.zzang.chongdae.data.source

import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.dto.request.AccessTokenRequest
import com.zzang.chongdae.remote.dto.response.auth.MemberResponse

interface AuthRemoteDataSource {
    suspend fun saveLogin(accessTokenRequest: AccessTokenRequest): Result<MemberResponse, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
