package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse, DataError.Network>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit, DataError.Network>
}
