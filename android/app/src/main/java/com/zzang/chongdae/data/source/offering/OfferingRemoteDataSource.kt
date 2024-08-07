package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.ProductUrlResponse

interface OfferingRemoteDataSource {
    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<OfferingsResponse>

    suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse>

    suspend fun fetchFilters(): Result<FiltersResponse>
}
