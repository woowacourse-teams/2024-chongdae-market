package com.zzang.chongdae.domain.usecase.login

interface CheckIfAlreadyLoggedInUseCase {
    suspend operator fun invoke(): Boolean
}
