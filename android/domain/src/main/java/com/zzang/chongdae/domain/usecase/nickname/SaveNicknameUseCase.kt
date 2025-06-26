package com.zzang.chongdae.domain.usecase.nickname

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveNicknameUseCase
    @Inject
    constructor(
        private val repository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(nickname: String) {
            repository.saveNickname(nickname)
        }
    }
