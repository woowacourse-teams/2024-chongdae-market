package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse

interface OfferingsDataSource {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): Result<OfferingsResponse>
}
