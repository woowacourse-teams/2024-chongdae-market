package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Article
import com.zzang.chongdae.domain.model.ArticleDetail
import com.zzang.chongdae.domain.model.Participation

interface GroupPurchaseRepository {
    suspend fun fetchGroupPurchases(): Result<List<Article>>

    suspend fun fetchGroupPurchaseDetail(id: Long): Result<ArticleDetail>

    suspend fun participateGroupPurchase(articleId: Long): Result<Participation>
}
