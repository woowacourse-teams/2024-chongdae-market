package com.zzang.chongdae.domain.repository

interface OfferingWriteRepository {
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
