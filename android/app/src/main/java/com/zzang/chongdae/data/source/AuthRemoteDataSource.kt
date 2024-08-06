package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.CiRequest
import com.zzang.chongdae.data.remote.dto.response.MemberResponse

interface AuthRemoteDataSource {
    suspend fun saveLogin(ci: String): Result<Unit>

    // 토큰이 뭔지 잘 몰라서 미구현. 내일 백엔드한테 물어보고 구현함
//    suspend fun saveRefresh()

    suspend fun saveSignup(ciRequest: CiRequest): Result<MemberResponse>
}
