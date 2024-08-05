package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.source.OfferingsDataSource
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingsRepository

class OfferingsRepositoryImpl(
    private val offeringsDataSource: OfferingsDataSource,
) : OfferingsRepository {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering> {
        return offeringsDataSource.fetchOfferings(lastOfferingId, pageSize).mapCatching {
            it.offerings.map { it.toDomain() }
        }.getOrThrow()
    }

    override suspend fun saveOffering(
        memberId: Long,
        title: String,
        productUrl: String?,
        thumbnailUrl: String?,
        totalCount: Int,
        totalPrice: Int,
        eachPrice: Int?,
        meetingAddress: String,
        meetingAddressDong: String?,
        meetingAddressDetail: String,
        deadline: String,
        description: String,
    ): Result<Unit> {
        return offeringsDataSource.saveOffering(
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
