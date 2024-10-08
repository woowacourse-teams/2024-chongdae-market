package com.zzang.chongdae.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.model.OfferingWrite
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.util.TestFixture
import okhttp3.MultipartBody

class FakeOfferingRepository : OfferingRepository {
    override suspend fun fetchOffering(offeringId: Long): Result<Offering, DataError.Network> {
        return Result.Success(
            Offering(
                id = 0,
                title = "",
                meetingAddressDong = "",
                thumbnailUrl = null,
                totalCount = 0,
                currentCount = 0,
                dividedPrice = 0,
                originPrice = null,
                status = OfferingCondition.CONFIRMED,
                isOpen = false,
            ),
        )
    }

    override suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<List<Offering>, DataError.Network> {
        return Result.Success(
            listOf(
                Offering(
                    id = 0,
                    title = "",
                    meetingAddressDong = "",
                    thumbnailUrl = null,
                    totalCount = 0,
                    currentCount = 0,
                    dividedPrice = 0,
                    originPrice = null,
                    status = OfferingCondition.CONFIRMED,
                    isOpen = false,
                ),
            ),
        )
    }

    override suspend fun saveOffering(offeringWrite: OfferingWrite): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl, DataError.Network> {
        return Result.Success(TestFixture.productUrl)
    }

    override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl, DataError.Network> {
        return Result.Success(TestFixture.productUrl)
    }

    override suspend fun fetchFilters(): Result<List<Filter>, DataError.Network> {
        return Result.Success(TestFixture.FILTERS_STUB)
    }

    override suspend fun fetchMeetings(offeringId: Long): Result<Meetings, DataError.Network> =
        Result.Success(
            TestFixture.meetings,
        )

    override suspend fun patchOffering(
        offeringId: Long,
        offeringModifyDomainRequest: OfferingModifyDomainRequest,
    ): Result<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }
}
