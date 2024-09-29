package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.di.annotations.OfferingApiServiceQualifier
import com.zzang.chongdae.di.annotations.ParticipantApiServiceQualifier
import javax.inject.Inject

class OfferingDetailDataSourceImpl
    @Inject
    constructor(
        @OfferingApiServiceQualifier private val offeringApiService: OfferingApiService,
        @ParticipantApiServiceQualifier private val participationApiService: ParticipationApiService,
    ) : OfferingDetailDataSource {
        override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network> =
            safeApiCall { offeringApiService.getOfferingDetail(offeringId) }

        override suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network> =
            safeApiCall { participationApiService.postParticipations(participationRequest) }

    }
