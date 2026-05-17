package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.offering.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import javax.inject.Inject

class FetchOfferingDetailUseCase
    @Inject
    constructor(
        private val offeringDetailRepository: OfferingDetailRepository,
    ) {
        suspend operator fun invoke(offeringId: Long): Result<OfferingDetail, DataError.Network> =
            offeringDetailRepository.fetchOfferingDetail(offeringId)
    }
