package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNotificationActivateUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        suspend operator fun invoke(): Boolean = userPreferencesDataStore.notificationActivateFlow.first()
    }
