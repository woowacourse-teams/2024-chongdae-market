package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.response.analytics.UserTypeResponse

interface AnalyticsDataSource {
    suspend fun fetchUserType(): com.zzang.chongdae.common.handler.Result<UserTypeResponse, com.zzang.chongdae.common.handler.DataError.Network>
}
