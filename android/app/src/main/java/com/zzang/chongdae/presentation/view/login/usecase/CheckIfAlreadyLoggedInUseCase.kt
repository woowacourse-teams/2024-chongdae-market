package com.zzang.chongdae.presentation.view.login.usecase

interface CheckIfAlreadyLoggedInUseCase {
    suspend operator fun invoke(): Boolean
}
