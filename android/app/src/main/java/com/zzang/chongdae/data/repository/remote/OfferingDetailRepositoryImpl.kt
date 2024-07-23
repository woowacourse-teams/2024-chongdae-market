package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.Participation
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailRepositoryImpl(
    private val offeringDetailDataSource: OfferingDetailDataSource,
) : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(
        offeringId: Long,
        memberId: Long
    ): Result<OfferingDetail> = offeringDetailDataSource.fetchOfferingDetail(
        offeringId = offeringId,
        memberId = memberId
    ).mapCatching {
        it.toDomain()
    }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Participation> =
        offeringDetailDataSource.saveParticipation(participationRequest).mapCatching {
            it.toDomain()
        }
}
