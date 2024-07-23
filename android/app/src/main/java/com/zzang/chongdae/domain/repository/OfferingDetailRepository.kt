package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchGroupPurchaseDetail(id: Long): Result<OfferingDetail>
}
