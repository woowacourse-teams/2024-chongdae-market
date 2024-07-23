package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long, memberId: Long): Result<OfferingDetail>
}
