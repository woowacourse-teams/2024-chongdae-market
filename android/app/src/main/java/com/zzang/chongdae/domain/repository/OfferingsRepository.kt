package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Offering

interface OfferingsRepository {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering>
}
