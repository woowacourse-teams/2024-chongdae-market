package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest

interface PostOfferingModifyUseCase {
    suspend operator fun invoke(
        offeringId: Long,
        offeringModifyDomainRequest: OfferingModifyDomainRequest,
    ): Result<Unit, DataError.Network>
}
