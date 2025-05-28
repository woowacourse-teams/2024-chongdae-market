package com.zzang.chongdae.domain.usecase.mypage

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import javax.inject.Inject

class SetNotificationActivateUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        suspend operator fun invoke(isActivate: Boolean) {
            userPreferencesDataStore.setNotificationActivate(isActivate)
        }
    }
