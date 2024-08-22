package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel
import okhttp3.MultipartBody

interface OfferingRepository {
    suspend fun fetchOffering(offeringId: Long): Result<Offering, DataError.Network>

    suspend fun fetchOfferings(
        filter: String?,
        search: String?,
        lastOfferingId: Long?,
        pageSize: Int?,
    ): Result<List<Offering>, DataError.Network>

    suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit, DataError.Network>

    suspend fun saveProductImageOg(productUrl: String): Result<ProductUrl, DataError.Network>

    suspend fun saveProductImageS3(image: MultipartBody.Part): Result<ProductUrl, DataError.Network>

    suspend fun fetchFilters(): Result<List<Filter>, DataError.Network>

    suspend fun fetchMeetings(offeringId: Long): Result<Meetings, DataError.Network>
}
