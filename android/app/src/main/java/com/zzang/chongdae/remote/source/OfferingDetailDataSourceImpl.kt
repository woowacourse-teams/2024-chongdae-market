package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class OfferingDetailDataSourceImpl(
    private val offeringApiService: OfferingApiService,
    private val participationApiService: ParticipationApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network> =
        safeApiCall { offeringApiService.getOfferingDetail(offeringId) }

    override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network> =
        safeApiCall { participationApiService.postParticipations(participationRequest) }
}
