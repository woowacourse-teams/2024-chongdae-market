package com.zzang.chongdae.auth.domain.usecase

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IsUserLoggedInUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(): Boolean {
            return userPreferencesRepository.accessTokenFlow.first() != null
        }
    }
