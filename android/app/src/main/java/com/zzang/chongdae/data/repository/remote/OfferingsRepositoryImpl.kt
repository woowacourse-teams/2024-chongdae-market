package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.source.OfferingsDataSource
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingsRepository
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel

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

    override suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit> {
        return offeringsDataSource.saveOffering(
            offeringWriteRequest =
                OfferingWriteRequest(
                    memberId = uiModel.memberId,
                    title = uiModel.title,
                    productUrl = uiModel.productUrl,
                    thumbnailUrl = uiModel.thumbnailUrl,
                    totalCount = uiModel.totalCount,
                    totalPrice = uiModel.totalPrice,
                    eachPrice = uiModel.eachPrice,
                    meetingAddress = uiModel.meetingAddress,
                    meetingAddressDong = uiModel.meetingAddressDong,
                    meetingAddressDetail = uiModel.meetingAddressDetail,
                    deadline = uiModel.deadline,
                    description = uiModel.description,
                ),
        )
    }
}
