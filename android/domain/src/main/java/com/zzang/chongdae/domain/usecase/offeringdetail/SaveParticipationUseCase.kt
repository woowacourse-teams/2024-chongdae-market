package com.zzang.chongdae.domain.usecase.offeringdetail

import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import javax.inject.Inject

class SaveParticipationUseCase
    @Inject
    constructor(
        val offeringDetailRepository: OfferingDetailRepository,
    ) {
        suspend operator fun invoke(
            offeringId: Long,
            participationCount: Int,
        ) = offeringDetailRepository.saveParticipation(offeringId, participationCount)
    }
