package com.zzang.chongdae.domain.usecase.auth

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import javax.inject.Inject

class RefreshTokenUseCase
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): com.zzang.chongdae.common.handler.Result<Unit, com.zzang.chongdae.common.handler.DataError.Network> {
            return authRepository.saveRefresh()
        }
    }
