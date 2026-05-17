package com.zzang.chongdae.domain.usecase.nickname

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetNickNameUseCase
    @Inject
    constructor(
        private val repository: UserPreferencesRepository,
    ) {
        operator fun invoke() = repository.nickNameFlow
    }
