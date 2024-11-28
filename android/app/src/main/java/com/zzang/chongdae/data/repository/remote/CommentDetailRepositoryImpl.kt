package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.source.CommentDetailDataSource
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.repository.CommentDetailRepository

class CommentDetailRepositoryImpl(
    private val commentDetailDataSource: CommentDetailDataSource,
) : CommentDetailRepository {
    override suspend fun getMeetings(offeringId: Long): Result<Meetings> {
        return commentDetailDataSource.getMeetings(offeringId).mapCatching {
            it.toDomain()
        }
    }
}
