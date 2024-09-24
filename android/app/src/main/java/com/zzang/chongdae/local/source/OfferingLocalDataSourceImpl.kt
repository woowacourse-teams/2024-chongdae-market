package com.zzang.chongdae.local.source

import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.local.dao.OfferingDao
import com.zzang.chongdae.local.model.OfferingEntity

class OfferingLocalDataSourceImpl(private val offeringDao: OfferingDao) : OfferingLocalDataSource {
    override suspend fun insertOfferings(offerings: List<OfferingEntity>) {
        offeringDao.insertAll(offerings)
    }

    override suspend fun insertOffering(offering: OfferingEntity) {
        offeringDao.insertOffering(offering)
    }
}
