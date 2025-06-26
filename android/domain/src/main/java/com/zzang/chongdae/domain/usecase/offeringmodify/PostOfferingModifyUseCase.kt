package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.offeringwrite.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostOfferingModifyUseCase
    @Inject
    constructor(
        private val offeringRepository: OfferingRepository,
    ) {
        suspend operator fun invoke(
            offeringId: Long,
            request: OfferingModifyDomainRequest,
        ): Result<Unit, DataError.Network> = offeringRepository.patchOffering(offeringId, request)
    }
