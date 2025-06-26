package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostProductImageOgUseCase
    @Inject
    constructor(
        private val offeringRepository: OfferingRepository,
    ) {
        suspend operator fun invoke(productUrl: String): Result<ProductUrl, DataError.Network> =
            offeringRepository.saveProductImageOg(productUrl)
    }
