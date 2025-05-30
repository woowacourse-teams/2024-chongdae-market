package com.zzang.chongdae.domain.usecase.nickname

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import javax.inject.Inject

class GetNickNameUseCase
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) {
        operator fun invoke() = userPreferencesDataStore.nickNameFlow
    }
