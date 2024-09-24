package com.zzang.chongdae.data.source.offering

import com.zzang.chongdae.local.model.OfferingEntity

interface OfferingLocalDataSource {
    suspend fun insertOfferings(offerings: List<OfferingEntity>)

    suspend fun insertOffering(offering: OfferingEntity)
}
