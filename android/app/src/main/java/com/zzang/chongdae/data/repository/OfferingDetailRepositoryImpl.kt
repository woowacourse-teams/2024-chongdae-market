package com.zzang.chongdae.data.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.mapper.toDomain
import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository

class OfferingDetailRepositoryImpl(
    private val offeringDetailDataSource: OfferingDetailDataSource,
) : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail, DataError.Network> =
        offeringDetailDataSource.fetchOfferingDetail(
            offeringId = offeringId,
        ).map {
            it.toDomain()
        }

    override suspend fun saveParticipation(offeringId: Long): Result<Unit, DataError.Network> =
        offeringDetailDataSource.saveParticipation(
            participationRequest = ParticipationRequest(offeringId),
        )
}
