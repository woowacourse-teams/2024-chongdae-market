package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.api.AnalyticsApiService
import com.zzang.chongdae.data.remote.dto.response.analytics.UserTypeResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.AnalyticsDataSource
import javax.inject.Inject

class AnalyticsDataSourceImpl
    @Inject
    constructor(
        private val analyticsApiService: AnalyticsApiService,
    ) : AnalyticsDataSource {
        override suspend fun fetchUserType(): Result<UserTypeResponse, DataError.Network> {
            return safeApiCall { analyticsApiService.getUserType() }
        }
    }
