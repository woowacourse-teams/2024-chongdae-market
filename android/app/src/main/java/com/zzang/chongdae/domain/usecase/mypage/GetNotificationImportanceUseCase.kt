package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNotificationImportanceUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        suspend operator fun invoke(): Int = userPreferencesDataStore.notificationImportanceFlow.first()
    }
