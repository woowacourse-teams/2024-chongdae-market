package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.mapper.toProductUrlRequest
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import okhttp3.MultipartBody
import retrofit2.Response

class OfferingRemoteDataSourceImpl(
    private val service: OfferingApiService,
) : OfferingRemoteDataSource {
    override suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<OfferingsResponse> =
        runCatching {
            service.getOfferings(filter, search, lastOfferingId, pageSize).body()
                ?: throw IllegalStateException()
        }

    override suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit> {
        return runCatching {
            val response = service.postOfferingWrite(offeringWriteRequest)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }
    }

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse> {
        return runCatching {
            val response: Response<ProductUrlResponse> =
                service.postProductImageOg(productUrl.toProductUrlRequest())
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }
    }

    override suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrlResponse> {
        return runCatching {
            val response: Response<ProductUrlResponse> = service.postProductImageS3(image)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }
    }

    override suspend fun fetchFilters(): Result<FiltersResponse> =
        runCatching {
            service.getFilters().body() ?: throw IllegalStateException()
        }

    override suspend fun fetchMeetings(offeringId: Long): Result<MeetingsResponse> {
        return runCatching {
            val response: Response<MeetingsResponse> = service.getMeetings(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }
    }

    companion object {
        private const val ERROR_PREFIX = "에러 발생: "
        private const val ERROR_NULL_MESSAGE = "null"
    }
}
