package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingsRepository

class OfferingsRepositoryImpl(
    private val offeringLocalDataSource: OfferingLocalDataSource,
    private val offeringRemoteDataSource: OfferingRemoteDataSource,
) : OfferingsRepository {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering> {
        return offeringRemoteDataSource.fetchOfferings(lastOfferingId, pageSize).mapCatching {
            it.offerings.map { it.toDomain() }
        }.getOrThrow()
    }
}
