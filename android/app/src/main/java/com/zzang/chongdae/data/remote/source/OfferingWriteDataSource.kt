package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest

interface OfferingWriteDataSource {
    suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit>
}
