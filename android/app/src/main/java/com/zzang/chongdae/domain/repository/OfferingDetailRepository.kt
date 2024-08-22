package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail, DataError.Network>

    suspend fun saveParticipation(offeringId: Long): Result<Unit, DataError.Network>
}
