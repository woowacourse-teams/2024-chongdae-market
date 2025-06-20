package com.zzang.chongdae.data.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.mapper.analytics.toDomain
import com.zzang.chongdae.data.source.AnalyticsDataSource
import com.zzang.chongdae.di.annotations.AnalyticsDataSourceQualifier
import com.zzang.chongdae.domain.model.analytics.UserType
import com.zzang.chongdae.domain.repository.AnalyticsRepository
import javax.inject.Inject

class AnalyticsRepositoryImpl
    @Inject
    constructor(
        @AnalyticsDataSourceQualifier
        private val analyticsDataSource: AnalyticsDataSource,
    ) : AnalyticsRepository {
        override suspend fun fetchUserType(): Result<UserType, DataError.Network> {
            return analyticsDataSource.fetchUserType()
                .map { it.toDomain() }
        }
    }
