package com.zzang.chongdae.data.local.source

import com.zzang.chongdae.data.local.dao.OfferingDao
import com.zzang.chongdae.data.local.model.OfferingEntity
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.di.annotations.OfferingDaoQualifier
import javax.inject.Inject

class OfferingLocalDataSourceImpl
    @Inject
    constructor(
        @OfferingDaoQualifier private val offeringDao: OfferingDao,
    ) : OfferingLocalDataSource {
        override suspend fun insertOfferings(offerings: List<OfferingEntity>) {
            offeringDao.insertAll(offerings)
        }

        override suspend fun insertOffering(offering: OfferingEntity) {
            offeringDao.insertOffering(offering)
        }
    }
