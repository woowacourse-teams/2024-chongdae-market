package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.OfferingDetail

interface FetchOfferingDetailUseCase {
    suspend operator fun invoke(offeringId: Long): Result<OfferingDetail, DataError.Network>
}
