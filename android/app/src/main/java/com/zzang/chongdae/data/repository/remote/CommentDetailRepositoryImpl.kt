package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.source.CommentDetailDataSource
import com.zzang.chongdae.domain.model.Comment
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

    override suspend fun saveParticipation(
        memberId: Long,
        offeringId: Long,
        comment: String,
    ): Result<Unit> =
        commentDetailDataSource.saveComment(
            commentRequest = CommentRequest(memberId, offeringId, comment),
        )

    override suspend fun fetchComments(
        offeringId: Long,
        memberId: Long,
    ): Result<List<Comment>> {
        return commentDetailDataSource.fetchComments(
            offeringId,
            memberId,
        ).mapCatching { response ->
            response.commentsResponse.map { it.toDomain() }
        }
    }
}
