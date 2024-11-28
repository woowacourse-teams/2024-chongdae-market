package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering
import okhttp3.MultipartBody

interface OfferingRemoteDataSource {
    suspend fun fetchOffering(offeringId: Long): Result<RemoteOffering, DataError.Network>

    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<OfferingsResponse, DataError.Network>

    suspend fun saveOffering(offeringWriteRequest: OfferingWriteRequest): Result<Unit, DataError.Network>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrlResponse, DataError.Network>

    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrlResponse, DataError.Network>

    suspend fun fetchFilters(): Result<FiltersResponse, DataError.Network>

    suspend fun fetchMeetings(offeringId: Long): Result<MeetingsResponse, DataError.Network>

    suspend fun patchOffering(
        offeringId: Long,
        offeringModifyRequest: OfferingModifyRequest,
    ): Result<Unit, DataError.Network>
}
