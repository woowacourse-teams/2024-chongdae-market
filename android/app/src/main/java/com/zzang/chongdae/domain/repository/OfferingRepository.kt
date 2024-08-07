package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import okhttp3.MultipartBody

interface OfferingRepository {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering>

    suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl>
    
    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl>
}
