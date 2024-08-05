package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.presentation.view.write.OfferingWriteUiModel

interface OfferingsRepository {
    suspend fun fetchOfferings(
        lastOfferingId: Long,
        pageSize: Int,
    ): List<Offering>

    suspend fun saveOffering(uiModel: OfferingWriteUiModel): Result<Unit>
}
