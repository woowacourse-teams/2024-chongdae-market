package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.util.TestFixture

class FakeOfferingDetailRepository : OfferingDetailRepository {
    override suspend fun fetchOfferingDetail(
        memberId: Long,
        offeringId: Long,
    ): Result<OfferingDetail> {
        return Result.success(TestFixture.OFFERING_DETAIL_STUB)
    }

    override suspend fun saveParticipation(
        memberId: Long,
        offeringId: Long,
    ): Result<Unit> {
        return Result.success(Unit)
    }
}
