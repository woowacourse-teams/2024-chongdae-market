package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import retrofit2.http.Query

interface OfferingRepository {
    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): List<Offering>

    suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl>

    suspend fun fetchFilters(): Result<List<Filter>>
}
