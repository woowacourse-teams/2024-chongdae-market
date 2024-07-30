package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.OfferingsApiService
import com.zzang.chongdae.data.remote.api.ParticipationsApiService
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource

class OfferingDetailDataSourceImpl(
    private val offeringsApiService: OfferingsApiService,
    private val participationsApiService: ParticipationsApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(
        offeringId: Long,
        memberId: Long,
    ): Result<OfferingDetailResponse> =
        runCatching {
            offeringsApiService.getOfferingDetail(offeringId = offeringId, memberId = memberId)
                .body()
                ?: throw IllegalStateException()
        }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit> =
        runCatching {
            participationsApiService.postParticipations(participationRequest = participationRequest)
                .body() ?: throw IllegalStateException()
        }
}
