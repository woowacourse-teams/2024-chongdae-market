package com.zzang.chongdae.presentation.view.write.usecase

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.OfferingWrite

interface PostOfferingUseCase {
    suspend operator fun invoke(offeringWrite: OfferingWrite): Result<Unit, DataError.Network>
}