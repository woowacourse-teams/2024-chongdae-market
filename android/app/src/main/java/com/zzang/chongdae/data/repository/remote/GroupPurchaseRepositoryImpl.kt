package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.source.GroupPurchaseDataSource
import com.zzang.chongdae.domain.model.Article
import com.zzang.chongdae.domain.model.Participation
import com.zzang.chongdae.domain.repository.GroupPurchaseRepository

class GroupPurchaseRepositoryImpl(
    private val groupPurchaseDataSource: GroupPurchaseDataSource,
) : GroupPurchaseRepository {
    override suspend fun fetchGroupPurchases(): Result<List<Article>> {
        return groupPurchaseDataSource.fetchGroupPurchases().mapCatching {
            it.responses.map { it.toDomain() }
        }
    }

    override suspend fun participateGroupPurchase(articleId: Long): Result<Participation> {
        return groupPurchaseDataSource.participateGroupPurchase(
            participationRequest = ParticipationRequest(articleId),
        ).mapCatching { it.toDomain() }
    }
}
