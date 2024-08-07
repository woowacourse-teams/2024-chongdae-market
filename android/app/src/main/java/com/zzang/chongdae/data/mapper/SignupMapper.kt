package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.SignupResponse
import com.zzang.chongdae.domain.mo.Member
import com.zzang.chongdae.domain.model.Signup
import com.zzang.chongdae.domain.model.Token

fun SignupResponse.toDomain(): Signup {
    return Signup(
        member = Member(this.memberResponse.memberId, this.memberResponse.nickname),
        token = Token(this.tokenResponse.accessToken, this.tokenResponse.refreshToken)
    )
}
