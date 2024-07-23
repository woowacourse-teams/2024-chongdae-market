package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailRepositoryImpl(
    private val offeringDetailDataSource: OfferingDetailDataSource,
) : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(
        offeringId: Long,
        memberId: Long
    ): Result<OfferingDetail> {
        return offeringDetailDataSource.fetchOfferingDetail(
            offeringId = offeringId,
            memberId = memberId
        ).mapCatching {
            it.toDomain()
        }
    }
}
