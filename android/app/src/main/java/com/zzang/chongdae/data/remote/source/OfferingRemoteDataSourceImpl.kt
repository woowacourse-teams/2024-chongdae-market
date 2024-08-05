package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource

class OfferingRemoteDataSourceImpl(
    private val service: OfferingApiService,
) : OfferingRemoteDataSource {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): Result<OfferingsResponse> =
        runCatching {
            service.getOfferings(lastOfferingId, pageSize).body() ?: throw IllegalStateException()
        }
}
