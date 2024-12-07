package com.zzang.chongdae.presentation.view.login.usecase

import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckIfAlreadyLoggedInUseCaseImpl
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : CheckIfAlreadyLoggedInUseCase {
        override suspend fun invoke(): Boolean {
            val accessToken = userPreferencesDataStore.accessTokenFlow.first()
            return accessToken != null
        }
    }
