package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingStatus
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import okhttp3.MultipartBody

interface OfferingRepository {
    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): List<Offering>

    suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl>

    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl>

    suspend fun fetchFilters(): Result<List<Filter>>

    suspend fun fetchOfferingStatus(offeringId: Long): Result<OfferingStatus>
}
