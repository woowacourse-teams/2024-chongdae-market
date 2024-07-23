package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Article

interface GroupPurchaseRepository {
    suspend fun fetchGroupPurchases(): Result<List<Article>>
}
