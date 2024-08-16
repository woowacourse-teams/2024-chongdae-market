package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse

interface AuthRemoteDataSource {
    suspend fun saveLogin(ciRequest: CiRequest): Result<Unit>

    suspend fun saveRefresh(): Result<Unit>

    suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse>
}
