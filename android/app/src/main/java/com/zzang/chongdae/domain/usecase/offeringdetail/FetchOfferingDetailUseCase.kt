package com.zzang.chongdae.domain.usecase.offeringdetail

import com.zzang.chongdae.di.annotations.OfferingDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import javax.inject.Inject

class FetchOfferingDetailUseCase
    @Inject
    constructor(
        @OfferingDetailRepositoryQualifier val offeringDetailRepository: OfferingDetailRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = offeringDetailRepository.fetchOfferingDetail(offeringId)
    }
