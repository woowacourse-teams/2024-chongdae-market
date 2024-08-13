package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail>

    suspend fun saveParticipation(offeringId: Long): Result<Unit>
}
