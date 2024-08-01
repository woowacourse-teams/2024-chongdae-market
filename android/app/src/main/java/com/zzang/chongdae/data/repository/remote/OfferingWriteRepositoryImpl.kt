package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.source.OfferingWriteDataSource
import com.zzang.chongdae.domain.repository.OfferingWriteRepository

class OfferingWriteRepositoryImpl(
    private val offeringWriteDataSource: OfferingWriteDataSource,
) : OfferingWriteRepository {
    override suspend fun saveOffering(
        memberId: Long,
        title: String,
        productUrl: String?,
        thumbnailUrl: String?,
        totalCount: Int,
        totalPrice: Int,
        eachPrice: Int,
        meetingAddress: String,
        meetingAddressDong: String?,
        meetingAddressDetail: String,
        deadline: String,
        description: String,
    ): Result<Unit> {
        return offeringWriteDataSource.saveOffering(
            offeringWriteRequest =
                OfferingWriteRequest(
                    memberId = memberId,
                    title = title,
                    productUrl = productUrl,
                    thumbnailUrl = thumbnailUrl,
                    totalCount = totalCount,
                    totalPrice = totalPrice,
                    eachPrice = eachPrice,
                    meetingAddress = meetingAddress,
                    meetingAddressDong = meetingAddressDong,
                    meetingAddressDetail = meetingAddressDetail,
                    deadline = deadline,
                    description = description,
                ),
        )
    }
}
