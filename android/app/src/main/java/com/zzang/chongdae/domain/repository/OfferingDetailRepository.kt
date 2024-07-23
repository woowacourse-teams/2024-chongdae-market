package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.Participation

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long, memberId: Long): Result<OfferingDetail>

    suspend fun saveParticipation(participationRequest: ParticipationRequest): Result<Participation>
}
