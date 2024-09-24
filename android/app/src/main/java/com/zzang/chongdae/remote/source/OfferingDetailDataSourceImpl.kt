package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.api.OfferingApiService
import com.zzang.chongdae.remote.api.ParticipationApiService
import com.zzang.chongdae.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.remote.util.safeApiCall

class OfferingDetailDataSourceImpl(
    private val offeringApiService: OfferingApiService,
    private val participationApiService: ParticipationApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network> =
        safeApiCall { offeringApiService.getOfferingDetail(offeringId) }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network> =
        safeApiCall { participationApiService.postParticipations(participationRequest) }
}
