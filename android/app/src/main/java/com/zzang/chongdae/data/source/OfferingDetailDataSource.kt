package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network>

    suspend fun deleteOffering(offeringId: Long): Result<Unit, DataError.Network>
}
