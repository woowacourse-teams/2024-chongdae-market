package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.domain.model.OfferingDetail

interface OfferingDetailRepository {
    suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail, DataError.Network>

    suspend fun saveParticipation(offeringId: Long): Result<Unit, DataError.Network>

    suspend fun patchOffering(
        offeringId: Long,
        offeringModifyRequest: OfferingModifyRequest,
    ): Result<OfferingDetail, DataError.Network>
}
