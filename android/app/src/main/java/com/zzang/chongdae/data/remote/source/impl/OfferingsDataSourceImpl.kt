package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.OfferingsApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.source.OfferingsDataSource

class OfferingsDataSourceImpl(
    private val service: OfferingsApiService,
) : OfferingsDataSource {
    override suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): Result<OfferingsResponse> =
        runCatching {
            service.getArticles(lastOfferingId, pageSize).body() ?: throw IllegalStateException()
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
}
