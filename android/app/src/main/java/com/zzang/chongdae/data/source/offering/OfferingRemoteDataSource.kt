package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.ProductUrlResponse
import retrofit2.http.Query

interface OfferingRemoteDataSource {
    suspend fun fetchOfferings(
        filter: String? = null,
        search: String? = null,
        lastOfferingId: Long? = null,
        pageSize: Int? = null,
    ): Result<OfferingsResponse>

    suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse>
}
