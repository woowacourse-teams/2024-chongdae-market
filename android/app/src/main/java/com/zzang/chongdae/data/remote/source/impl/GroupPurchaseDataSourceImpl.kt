package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.GroupPurchaseApiService
import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse
import com.zzang.chongdae.data.remote.source.GroupPurchaseDataSource

class GroupPurchaseDataSourceImpl(
    private val service: GroupPurchaseApiService,
) : GroupPurchaseDataSource {
    override suspend fun fetchGroupPurchases(): Result<GroupPurchasesResponse> =
        runCatching {
            service.getArticles().body() ?: throw IllegalStateException()
        }
}
