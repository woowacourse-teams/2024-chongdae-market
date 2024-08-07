package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingStatus
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import okhttp3.MultipartBody

class OfferingRepositoryImpl(
    private val offeringLocalDataSource: OfferingLocalDataSource,
    private val offeringRemoteDataSource: OfferingRemoteDataSource,
) : OfferingRepository {
    override suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): List<Offering> {
        return offeringRemoteDataSource.fetchOfferings(filter, search, lastOfferingId, pageSize)
            .mapCatching {
                it.offerings.map { it.toDomain() }
            }.getOrThrow()
    }

    override suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit> {
        return offeringRemoteDataSource.saveOffering(
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

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl> {
        return offeringRemoteDataSource.saveProductImageOg(productUrl).mapCatching {
            it.toDomain()
        }
    }

    override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl> {
        return offeringRemoteDataSource.saveProductImageS3(image).mapCatching {
            it.toDomain()
        }
    }

    override suspend fun fetchFilters(): Result<List<Filter>> {
        return offeringRemoteDataSource.fetchFilters().mapCatching {
            it.filters.map { it.toDomain() }
        }
    }

    override suspend fun fetchOfferingStatus(offeringId: Long): Result<OfferingStatus> {
        return offeringRemoteDataSource.fetchOfferingStatus(offeringId).mapCatching {
            it.toDomain()
        }
    }

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit> {
        return offeringRemoteDataSource.updateOfferingStatus(offeringId)
    }
}
