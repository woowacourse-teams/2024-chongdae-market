package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.OfferingDetailApiService
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource

class OfferingDetailDataSourceImpl(
    private val service: OfferingDetailApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(
        offeringId: Long,
        memberId: Long,
    ): Result<OfferingDetailResponse> =
        runCatching {
            service.getOfferingDetail(offeringId = offeringId, memberId = memberId).body()
                ?: throw IllegalStateException()
        }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit> =
        runCatching {
            service.postParticipations(participationRequest = participationRequest)
                .body() ?: throw IllegalStateException()
        }
}
