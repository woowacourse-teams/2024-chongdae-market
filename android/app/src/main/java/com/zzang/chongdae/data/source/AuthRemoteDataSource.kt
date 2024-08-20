package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface AuthRemoteDataSource {
    suspend fun saveLogin(ciRequest: CiRequest): Result<MemberResponse, DataError.Network>

    suspend fun saveRefresh(): Result<Unit, DataError.Network>

    suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse, DataError.Network>
}
