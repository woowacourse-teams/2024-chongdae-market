package com.zzang.chongdae.domain.usecase.nickname

import com.zzang.chongdae.di.annotations.ParticipantRepositoryQualifier
import com.zzang.chongdae.domain.repository.ParticipantRepository
import javax.inject.Inject

class PatchNicknameUseCase
    @Inject
    constructor(
        @ParticipantRepositoryQualifier private val repository: ParticipantRepository,
    ) {
        suspend operator fun invoke(nickname: String) = repository.patchNickname(nickname)
    }
