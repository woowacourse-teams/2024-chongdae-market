package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailRepositoryImpl(
    private val offeringDetailDataSource: OfferingDetailDataSource,
) : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail> =
        offeringDetailDataSource.fetchOfferingDetail(
            offeringId = offeringId,
        ).mapCatching {
            it.toDomain()
        }

    override suspend fun saveParticipation(offeringId: Long): Result<Unit> =
        offeringDetailDataSource.saveParticipation(
            participationRequest = ParticipationRequest(offeringId),
        )
}
