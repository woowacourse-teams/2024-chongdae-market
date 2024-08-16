package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.MemberResponse

interface AuthRemoteDataSource {
    suspend fun saveLogin(ciRequest: CiRequest): Result<MemberResponse>

    suspend fun saveRefresh(): Result<Unit>

    suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse>
}
