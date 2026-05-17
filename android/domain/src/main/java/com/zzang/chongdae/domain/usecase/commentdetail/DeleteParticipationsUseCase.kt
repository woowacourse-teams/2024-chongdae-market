package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.domain.repository.ParticipantRepository
import javax.inject.Inject

class DeleteParticipationsUseCase
    @Inject
    constructor(
        private val repository: ParticipantRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = repository.deleteParticipations(offeringId)
    }
