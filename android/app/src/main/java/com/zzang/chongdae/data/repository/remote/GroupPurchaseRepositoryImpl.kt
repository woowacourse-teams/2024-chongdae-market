package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.ParticipationRequest
import com.zzang.chongdae.data.remote.source.GroupPurchaseDataSource
import com.zzang.chongdae.domain.model.Article
import com.zzang.chongdae.domain.model.ArticleDetail
import com.zzang.chongdae.domain.model.Participation
import com.zzang.chongdae.domain.repository.GroupPurchaseRepository

class GroupPurchaseRepositoryImpl(
    private val groupPurchaseDataSource: GroupPurchaseDataSource,
) : GroupPurchaseRepository {
    override suspend fun getGroupPurchases(): Result<List<Article>> {
        return groupPurchaseDataSource.getGroupPurchases().mapCatching {
            it.responses.map { it.toDomain() }
        }
    }

    override suspend fun getGroupPurchaseDetail(id: Long): Result<ArticleDetail> {
        return groupPurchaseDataSource.getGroupPurchaseDetail(id = id).mapCatching {
            it.toDomain()
        }
    }

    override suspend fun participateGroupPurchase(articleId: Long): Result<Participation> {
        return groupPurchaseDataSource.participateGroupPurchase(
            participationRequest = ParticipationRequest(articleId),
        ).mapCatching { it.toDomain() }
    }
}
