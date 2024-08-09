package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingStatus
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import com.zzang.chongdae.util.TestFixture
import okhttp3.MultipartBody

class FakeOfferingRepository : OfferingRepository {
    private var offeringStatus: OfferingStatus = TestFixture.offeringStatus

    override suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): List<Offering> {
        TODO("Not yet implemented")
    }

    override suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl> {
        return Result.success(TestFixture.productUrl)
    }

    override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl> {
        return Result.success(TestFixture.productUrl)
    }

    override suspend fun fetchFilters(): Result<List<Filter>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchOfferingStatus(offeringId: Long): Result<OfferingStatus> {
        return Result.success(
            offeringStatus,
        )
    }

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit> {
        offeringStatus = TestFixture.offeringStatus2
        return Result.success(Unit)
    }
}
