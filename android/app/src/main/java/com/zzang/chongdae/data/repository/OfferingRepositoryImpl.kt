package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.local.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import okhttp3.MultipartBody

class OfferingRepositoryImpl(
    private val offeringLocalDataSource: OfferingLocalDataSource,
    private val offeringRemoteDataSource: OfferingRemoteDataSource,
) : OfferingRepository {
    override suspend fun fetchOffering(offeringId: Long): Result<Offering, DataError.Network> =
        offeringRemoteDataSource.fetchOffering(offeringId = offeringId).map {
            it.toDomain()
        }

    override suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<List<Offering>, DataError.Network> {
        return offeringRemoteDataSource.fetchOfferings(filter, search, lastOfferingId, pageSize)
            .map {
                it.offerings.map { it.toDomain() }
            }
    }

    override suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit, DataError.Network> {
        return offeringRemoteDataSource.saveOffering(
            offeringWriteRequest =
                OfferingWriteRequest(
                    title = uiModel.title,
                    productUrl = uiModel.productUrl,
                    thumbnailUrl = uiModel.thumbnailUrl,
                    totalCount = uiModel.totalCount,
                    totalPrice = uiModel.totalPrice,
                    originPrice = uiModel.originPrice,
                    meetingAddress = uiModel.meetingAddress,
                    meetingAddressDong = uiModel.meetingAddressDong,
                    meetingAddressDetail = uiModel.meetingAddressDetail,
                    meetingDate = uiModel.meetingDate,
                    description = uiModel.description,
                ),
        )
    }

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl, DataError.Network> {
        return offeringRemoteDataSource.saveProductImageOg(productUrl).map {
            it.toDomain()
        }
    }

    override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl, DataError.Network> {
        return offeringRemoteDataSource.saveProductImageS3(image).map {
            it.toDomain()
        }
    }

    override suspend fun fetchFilters(): Result<List<Filter>, DataError.Network> {
        return offeringRemoteDataSource.fetchFilters().map {
            it.filters.map { it.toDomain() }
        }
    }

    override suspend fun fetchMeetings(offeringId: Long): Result<Meetings, DataError.Network> {
        return offeringRemoteDataSource.fetchMeetings(offeringId).map {
            it.toDomain()
        }
    }
}
