package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse

interface GroupPurchaseDataSource {
    suspend fun fetchGroupPurchases(): Result<GroupPurchasesResponse>
}
