package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetailResponse>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Unit>
}
