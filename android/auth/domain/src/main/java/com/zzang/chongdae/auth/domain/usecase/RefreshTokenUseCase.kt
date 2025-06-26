package com.zzang.chongdae.auth.domain.usecase

import com.zzang.chongdae.auth.domain.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import javax.inject.Inject

class RefreshTokenUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): Result<Unit, DataError.Network> {
            return authRepository.saveRefresh()
        }
    }
