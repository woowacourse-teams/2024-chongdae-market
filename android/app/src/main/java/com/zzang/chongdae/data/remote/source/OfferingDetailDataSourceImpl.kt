package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.data.source.OfferingDetailDataSource

class OfferingDetailDataSourceImpl(
    private val offeringApiService: OfferingApiService,
    private val participationApiService: ParticipationApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse> =
        runCatching {
            offeringApiService.getOfferingDetail(offeringId = offeringId)
                .body()
                ?: throw IllegalStateException()
        }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit> =
        runCatching {
            participationApiService.postParticipations(participationRequest = participationRequest)
                .body() ?: throw IllegalStateException()
        }
}
