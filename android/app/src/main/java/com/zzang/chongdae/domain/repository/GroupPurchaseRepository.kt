package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Article
import com.zzang.chongdae.domain.model.Participation

interface GroupPurchaseRepository {
    suspend fun fetchGroupPurchases(): Result<List<Article>>
}
