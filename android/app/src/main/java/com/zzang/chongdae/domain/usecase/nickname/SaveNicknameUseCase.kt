package com.zzang.chongdae.domain.usecase.nickname

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import javax.inject.Inject

class SaveNicknameUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        suspend operator fun invoke(nickname: String) {
            userPreferencesDataStore.saveNickname(nickname)
        }
    }
