package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNotificationActivateUseCase
    @Inject
    constructor(
        private val repository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(): Boolean = repository.notificationActivateFlow.first()
    }
