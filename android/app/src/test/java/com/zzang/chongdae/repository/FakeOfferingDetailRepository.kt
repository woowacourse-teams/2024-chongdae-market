package com.zzang.chongdae.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.util.TestFixture

class FakeOfferingDetailRepository : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(offeringId: Long): Result<OfferingDetail, DataError.Network> {
        return Result.Success(TestFixture.OFFERING_DETAIL_STUB)
    }

    override suspend fun saveParticipation(offeringId: Long): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }
}
