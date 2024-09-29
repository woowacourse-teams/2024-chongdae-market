package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network>

    suspend fun patchOffering(
        offeringId: Long,
        offeringModifyRequest: OfferingModifyRequest,
    ): Result<OfferingDetailResponse, DataError.Network>
}
