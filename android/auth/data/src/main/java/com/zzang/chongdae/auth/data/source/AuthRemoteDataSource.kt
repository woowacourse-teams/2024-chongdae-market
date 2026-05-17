package com.zzang.chongdae.auth.data.source

import com.zzang.chongdae.auth.data.dto.request.TokensRequest
import com.zzang.chongdae.auth.data.dto.response.MemberResponse
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

interface AuthRemoteDataSource {
    suspend fun saveLogin(tokensRequest: TokensRequest): Result<MemberResponse, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>
}
