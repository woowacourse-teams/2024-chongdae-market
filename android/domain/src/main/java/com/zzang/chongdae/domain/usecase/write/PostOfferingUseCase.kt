package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.offeringwrite.OfferingWrite
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostOfferingUseCase
    @Inject
    constructor(
        private val offeringRepository: OfferingRepository,
    ) {
        suspend operator fun invoke(offeringWrite: OfferingWrite): Result<Unit, DataError.Network> =
            offeringRepository.saveOffering(offeringWrite)
    }
