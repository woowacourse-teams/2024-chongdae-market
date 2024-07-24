package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.source.OfferingsDataSource
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingsRepository

class OfferingsRepositoryImpl(
    private val offeringsDataSource: OfferingsDataSource,
) : OfferingsRepository {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int
    ): Result<List<Offering>> {
        return offeringsDataSource.fetchOfferings(lastOfferingId, pageSize).mapCatching {
            it.offerings.map { it.toDomain() }
        }
    }
}
