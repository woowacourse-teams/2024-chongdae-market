package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse

interface OfferingRemoteDataSource {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): Result<OfferingsResponse>
}
