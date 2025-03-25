package com.zzang.chongdae.domain.usecase.offeringdetail

import com.zzang.chongdae.di.annotations.OfferingDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import javax.inject.Inject

class SaveParticipationUseCase
    @Inject
    constructor(
        @OfferingDetailRepositoryQualifier val offeringDetailRepository: OfferingDetailRepository,
    ) {
        suspend operator fun invoke(
            offeringId: Long,
            participationCount: Int,
        ) = offeringDetailRepository.saveParticipation(offeringId, participationCount)
    }
