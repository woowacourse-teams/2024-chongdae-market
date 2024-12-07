package com.zzang.chongdae.presentation.view.login.usecase

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result

interface PostLoginUseCase {
    suspend operator fun invoke(
        accessToken: String,
        fcmToken: String,
    ): Result<Unit, DataError.Network>
}
