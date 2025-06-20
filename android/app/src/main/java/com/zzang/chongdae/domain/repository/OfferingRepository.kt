package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.comment.Meetings
import com.zzang.chongdae.domain.model.offering.Filter
import com.zzang.chongdae.domain.model.offering.Offering
import com.zzang.chongdae.domain.model.offeringwrite.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.model.offeringwrite.OfferingWrite
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl
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
    ): Result<Unit, DataError.Network>
}
