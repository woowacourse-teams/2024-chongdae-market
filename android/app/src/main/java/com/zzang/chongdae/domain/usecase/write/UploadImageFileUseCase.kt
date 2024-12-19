package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.ProductUrl
import okhttp3.MultipartBody

interface UploadImageFileUseCase {
    suspend operator fun invoke(multipartBody: MultipartBody.Part): Result<ProductUrl, DataError.Network>
}
