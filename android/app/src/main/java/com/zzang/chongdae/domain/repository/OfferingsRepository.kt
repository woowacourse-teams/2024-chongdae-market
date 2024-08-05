package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Offering

interface OfferingsRepository {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering>

    suspend fun saveOffering(
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
    ): Result<Unit>
}
