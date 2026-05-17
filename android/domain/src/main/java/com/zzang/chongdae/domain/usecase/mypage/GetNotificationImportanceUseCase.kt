package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNotificationImportanceUseCase
    @Inject
    constructor(
        private val repository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(): Int = repository.notificationImportanceFlow.first()
    }
