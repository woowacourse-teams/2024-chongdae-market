package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(
        memberId: Long,
        offeringId: Long,
    ): Result<OfferingDetail>

    suspend fun saveParticipation(
        memberId: Long,
        offeringId: Long,
    ): Result<Unit>
}
