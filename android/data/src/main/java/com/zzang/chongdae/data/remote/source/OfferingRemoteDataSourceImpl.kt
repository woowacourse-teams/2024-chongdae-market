package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.network.safeApiCall
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering
import com.zzang.chongdae.data.remote.mapper.toProductUrlRequest
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import okhttp3.MultipartBody
import javax.inject.Inject

class OfferingRemoteDataSourceImpl
    @Inject
    constructor(
        private val service: com.zzang.chongdae.data.remote.api.OfferingApiService,
    ) : OfferingRemoteDataSource {
        override suspend fun fetchOffering(offeringId: Long): Result<com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering, DataError.Network> =
            safeApiCall { service.getOffering(offeringId) }

        override suspend fun fetchOfferings(
            filter: String?,
            search: String?,
            lastOfferingId: Long?,
            pageSize: Int?,
        ): Result<com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse, DataError.Network> =
            safeApiCall {
                service.getOfferings(filter, search, lastOfferingId, pageSize)
            }

        override suspend fun saveOffering(offeringWriteRequest: com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest): Result<Unit, DataError.Network> =
            safeApiCall { service.postOfferingWrite((offeringWriteRequest)) }

        override suspend fun saveProductImageOg(productUrl: String): Result<com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse, DataError.Network> =
            safeApiCall { service.postProductImageOg((productUrl.toProductUrlRequest())) }

        override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse, DataError.Network> =
            safeApiCall { service.postProductImageS3(image) }

        override suspend fun fetchFilters(): Result<com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse, DataError.Network> =
            safeApiCall {
                service.getFilters()
            }

        override suspend fun fetchMeetings(offeringId: Long): Result<com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse, DataError.Network> =
            safeApiCall { service.getMeetings(offeringId) }

        override suspend fun patchOffering(
            offeringId: Long,
            offeringModifyRequest: com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest,
        ): Result<Unit, DataError.Network> {
            return safeApiCall {
                service.patchOffering(
                    offeringId,
                    offeringModifyRequest,
                )
            }
        }

        companion object {
            private const val ERROR_PREFIX = "에러 발생: "
            private const val ERROR_NULL_MESSAGE = "null"
        }
    }
