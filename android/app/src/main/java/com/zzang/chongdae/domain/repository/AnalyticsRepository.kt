package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.analytics.UserType

interface AnalyticsRepository {
    suspend fun fetchUserType(): com.zzang.chongdae.common.handler.Result<UserType, com.zzang.chongdae.common.handler.DataError.Network>
}
