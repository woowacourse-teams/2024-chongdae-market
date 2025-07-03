package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(
        offeringId: Long,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse, DataError.Network>

    suspend fun saveParticipation(
        participationRequest: com.zzang.chongdae.data.remote.dto.request.ParticipationRequest,
    ): Result<Unit, DataError.Network>

    suspend fun deleteOffering(offeringId: Long): Result<Unit, DataError.Network>
}
