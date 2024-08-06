package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.mapper.toProductUrlRequest
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.ProductUrlResponse
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import retrofit2.Response

class OfferingRemoteDataSourceImpl(
    private val service: OfferingApiService,
) : OfferingRemoteDataSource {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): Result<OfferingsResponse> =
        runCatching {
            service.getOfferings(lastOfferingId, pageSize).body() ?: throw IllegalStateException()
        }

    override suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit> {
        return runCatching {
            val response = service.postOfferingWrite(offeringWriteRequest)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("에러 발생: ${response.code()}")
            }
        }
    }

    override suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse> {
        return runCatching {
            val response: Response<ProductUrlResponse> = service.postProductImageOg(productUrl.toProductUrlRequest())
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("에러 발생: ${response.code()}")
            }
        }
    }
}
