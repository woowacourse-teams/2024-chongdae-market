package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail, DataError.Network>

    suspend fun saveParticipation(offeringId: Long): Result<Unit, DataError.Network>
}
