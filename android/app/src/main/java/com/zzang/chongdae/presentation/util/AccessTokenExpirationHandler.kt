package com.zzang.chongdae.presentation.util

import android.util.Log
import com.zzang.chongdae.domain.model.HttpStatusCode
import com.zzang.chongdae.domain.repository.AuthRepository

suspend fun handleAccessTokenExpiration(
    authRepository: AuthRepository,
    it: Throwable,
    retryFunction: () -> Unit,
) {
    when (it.message) {
        HttpStatusCode.UNAUTHORIZED_401.code -> {
            Log.e("error", "Access Token 만료")
            authRepository.saveRefresh()
            retryFunction()
        }
    }
}