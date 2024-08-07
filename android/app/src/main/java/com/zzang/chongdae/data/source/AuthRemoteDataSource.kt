package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.SignupResponse
import com.zzang.chongdae.data.remote.dto.response.TokenResponse

interface AuthRemoteDataSource {
    suspend fun saveLogin(ciRequest: CiRequest): Result<TokenResponse>

    suspend fun saveSignup(ciRequest: CiRequest): Result<SignupResponse>
}
