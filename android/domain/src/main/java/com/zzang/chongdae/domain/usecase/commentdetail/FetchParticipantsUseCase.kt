package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.domain.repository.ParticipantRepository
import javax.inject.Inject

class FetchParticipantsUseCase
    @Inject
    constructor(
        private val repository: ParticipantRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = repository.fetchParticipants(offeringId)
    }
