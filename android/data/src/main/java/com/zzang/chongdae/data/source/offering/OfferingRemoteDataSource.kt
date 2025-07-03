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
    suspend fun fetchOffering(
        offeringId: Long,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering, DataError.Network>

    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.OfferingsResponse, DataError.Network>

    suspend fun saveOffering(
        offeringWriteRequest: com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest,
    ): Result<Unit, DataError.Network>

    suspend fun saveProductImageOg(
        productUrl: String,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse, DataError.Network>

    suspend fun saveProductImageS3(
        image: MultipartBody.Part,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse, DataError.Network>

    suspend fun fetchFilters(): Result<com.zzang.chongdae.data.remote.dto.response.offering.FiltersResponse, DataError.Network>

    suspend fun fetchMeetings(
        offeringId: Long,
    ): Result<com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse, DataError.Network>

    suspend fun patchOffering(
        offeringId: Long,
        offeringModifyRequest: com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest,
    ): Result<Unit, DataError.Network>
}
