package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.mapper.toDomain
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class CommentDetailRepositoryImpl(
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : CommentDetailRepository {
    override suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit, DataError.Network> {
        return commentRemoteDataSource.saveComment(
            CommentRequest(offeringId, comment),
        ).map { Unit }
    }

    override suspend fun fetchComments(offeringId: Long): Result<List<Comment>, DataError.Network> {
        return commentRemoteDataSource.fetchComments(offeringId)
            .map { response ->
                response.commentsResponse.map { it.toDomain() }
            }
    }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo, DataError.Network> {
        return commentRemoteDataSource.fetchCommentOfferingInfo(offeringId)
            .map { response ->
                response.toDomain()
            }
    }

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit, DataError.Network> {
        return commentRemoteDataSource.updateOfferingStatus(offeringId)
            .map { Unit }
    }
}
