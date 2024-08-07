package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Signup
import com.zzang.chongdae.domain.model.Token

interface AuthRepository {
    suspend fun saveLogin(ci: String): Result<Token>

    suspend fun saveSignup(ci: String): Result<Signup>
}
