package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.ProductUrl

interface PostProductImageOgUseCase {
    suspend operator fun invoke(productUrl: String): Result<ProductUrl, DataError.Network>
}
