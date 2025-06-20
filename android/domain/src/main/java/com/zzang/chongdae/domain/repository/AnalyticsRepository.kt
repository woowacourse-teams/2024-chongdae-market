package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.analytics.UserType

interface AnalyticsRepository {
    suspend fun fetchUserType(): Result<UserType, DataError.Network>
}
