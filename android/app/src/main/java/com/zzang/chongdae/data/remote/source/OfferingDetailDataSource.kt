package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse

interface OfferingDetailDataSource {
    suspend fun fetchOfferingDetail(id: Long): Result<OfferingDetailResponse>
}
