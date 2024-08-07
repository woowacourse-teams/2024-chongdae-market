package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.TokenResponse
import com.zzang.chongdae.domain.model.Token

fun TokenResponse.toDomain(): Token {
    return Token(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}