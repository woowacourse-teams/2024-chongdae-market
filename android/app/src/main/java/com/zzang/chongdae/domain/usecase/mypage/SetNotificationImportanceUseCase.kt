package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import javax.inject.Inject

class SetNotificationImportanceUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        suspend operator fun invoke(importance: Int) {
            userPreferencesDataStore.setNotificationImportance(importance)
        }
    }
