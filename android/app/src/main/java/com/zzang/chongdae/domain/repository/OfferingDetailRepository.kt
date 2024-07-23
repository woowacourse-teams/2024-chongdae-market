package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.Participation

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(memberId: Long, offeringId: Long): Result<OfferingDetail>

    suspend fun saveParticipation(memberId: Long, offeringId: Long): Result<Participation>
}
