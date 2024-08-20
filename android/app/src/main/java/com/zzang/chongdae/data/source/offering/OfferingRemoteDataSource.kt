package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering
import okhttp3.MultipartBody

interface OfferingRemoteDataSource {
    suspend fun fetchOffering(offeringId: Long): Result<RemoteOffering>

    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<OfferingsResponse>

    suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse>

    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrlResponse>

    suspend fun fetchFilters(): Result<FiltersResponse>

    suspend fun fetchMeetings(offeringId: Long): Result<MeetingsResponse>
}
