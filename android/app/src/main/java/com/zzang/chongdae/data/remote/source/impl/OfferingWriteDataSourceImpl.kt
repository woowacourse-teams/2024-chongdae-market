package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.OfferingsApiService
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.source.OfferingWriteDataSource

class OfferingWriteDataSourceImpl(
    private val offeringsApiService: OfferingsApiService,
) : OfferingWriteDataSource {
    override suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit> {
        return runCatching {
            val response = offeringsApiService.postOfferingWrite(offeringWriteRequest)
            if (response.isSuccessful) {
                response.body() ?: error("")
            } else {
                error("")
            }
        }
    }
}
