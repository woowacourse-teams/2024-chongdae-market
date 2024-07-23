package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.dto.response.ParticipationResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(
        offeringId: Long,
        memberId: Long
    ): Result<OfferingDetailResponse>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<ParticipationResponse>
}
