package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.OfferingDetailApiService
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSource

class OfferingDetailDataSourceImpl(
    private val service: OfferingDetailApiService,
) : OfferingDetailDataSource {
    override suspend fun fetchOfferingDetail(id: Long): Result<OfferingDetailResponse> =
        runCatching {
            service.getOfferingDetail(id = id).body() ?: throw IllegalStateException()
        }
}
