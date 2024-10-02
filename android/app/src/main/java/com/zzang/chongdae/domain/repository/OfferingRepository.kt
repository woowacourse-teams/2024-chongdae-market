package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.model.OfferingModifyDomainResponse
import com.zzang.chongdae.domain.model.OfferingWrite
import com.zzang.chongdae.domain.model.ProductUrl
import okhttp3.MultipartBody

interface OfferingRepository {
    suspend fun fetchOffering(offeringId: Long): Result<Offering, DataError.Network>

    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<List<Offering>, DataError.Network>

    suspend fun saveOffering(offeringWrite: OfferingWrite): Result<Unit, DataError.Network>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl, DataError.Network>

    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl, DataError.Network>

    suspend fun fetchFilters(): Result<List<Filter>, DataError.Network>

    suspend fun fetchMeetings(offeringId: Long): Result<Meetings, DataError.Network>

    suspend fun patchOffering(
        offeringId: Long,
        offeringModifyDomainRequest: OfferingModifyDomainRequest,
    ): Result<OfferingModifyDomainResponse, DataError.Network>
}
