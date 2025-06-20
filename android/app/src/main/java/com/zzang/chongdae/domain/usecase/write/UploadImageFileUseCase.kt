package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.offeringwrite.ProductImage
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl

interface UploadImageFileUseCase {
    suspend fun invoke(image: ProductImage): Result<ProductUrl, DataError.Network>
}
