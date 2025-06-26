package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class LogoutUseCase
    @Inject
    constructor(
        private val repository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke() {
            repository.removeAllData()
        }
    }
