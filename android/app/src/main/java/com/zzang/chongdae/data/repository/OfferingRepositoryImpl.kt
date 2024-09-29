package com.zzang.chongdae.data.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.mapper.toDomain
import com.zzang.chongdae.data.remote.mapper.toRequest
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.di.annotations.OfferingLocalDataSourceQualifier
import com.zzang.chongdae.di.annotations.OfferingRemoteDataSourceQualifier
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.OfferingModify
import com.zzang.chongdae.domain.model.OfferingWrite
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class OfferingRepositoryImpl
@Inject
constructor(
    @OfferingLocalDataSourceQualifier private val offeringLocalDataSource: OfferingLocalDataSource,
    @OfferingRemoteDataSourceQualifier private val offeringRemoteDataSource: OfferingRemoteDataSource,
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

    override suspend fun saveOffering(offeringWrite: OfferingWrite): Result<Unit, DataError.Network> {
        return offeringRemoteDataSource.saveOffering(
            offeringWriteRequest = offeringWrite.toRequest()
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

    override suspend fun patchOffering(
        offeringId: Long,
        offeringModify: OfferingModify,
    ): Result<OfferingDetail, DataError.Network> =
        offeringRemoteDataSource.patchOffering(
            offeringId,
            offeringModify.toRequest(),
        ).map {
            it.toDomain()
        }
}
