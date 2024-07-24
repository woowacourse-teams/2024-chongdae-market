package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailRepositoryImpl(
    private val offeringDetailDataSource: OfferingDetailDataSource,
) : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(
        memberId: Long,
        offeringId: Long,
    ): Result<OfferingDetail> =
        offeringDetailDataSource.fetchOfferingDetail(
            offeringId = offeringId,
            memberId = memberId,
        ).mapCatching {
            it.toDomain()
        }

    override suspend fun saveParticipation(
        memberId: Long,
        offeringId: Long,
    ): Result<Unit> =
        offeringDetailDataSource.saveParticipation(
            participationRequest = ParticipationRequest(memberId, offeringId),
        )
}
