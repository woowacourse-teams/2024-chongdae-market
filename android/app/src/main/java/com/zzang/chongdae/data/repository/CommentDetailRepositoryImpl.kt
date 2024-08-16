package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.local.model.OfferingEntity
import com.zzang.chongdae.data.mapper.mapToCommentEntity
import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.source.comment.CommentLocalDataSource
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.repository.CommentDetailRepository

class CommentDetailRepositoryImpl(
    private val offeringLocalDataSource: OfferingLocalDataSource,
    private val commentLocalDataSource: CommentLocalDataSource,
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : CommentDetailRepository {
    override suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit> =
        commentRemoteDataSource.saveComment(
            commentRequest = CommentRequest(offeringId, comment),
        )

    override suspend fun fetchComments(offeringId: Long): Result<List<Comment>> {
        return commentRemoteDataSource.fetchComments(
            offeringId,
        ).mapCatching { response ->
            response.commentsResponse.map { it.toDomain() }
        }
    }

    override suspend fun fetchCommentsWithRoom(offeringId: Long): Result<List<Comment>> {
        try {
            offeringLocalDataSource.insertOffering(OfferingEntity(offeringId))
            val response = commentRemoteDataSource.fetchComments(offeringId)
            return if (response.isSuccess) {
                val commentsResponse = response.getOrNull()
                if (commentsResponse != null) {
                    val newComments =
                        commentsResponse.commentsResponse.map {
                            mapToCommentEntity(
                                offeringId = offeringId,
                                commentResponse = it,
                            )
                        }
                    commentLocalDataSource.insertComments(newComments)

                    Result.success(commentsResponse.commentsResponse.map { it.toDomain() })
                } else {
                    Result.failure(Throwable("댓글 데이터가 없습니다."))
                }
            } else {
                return Result.failure(Throwable("서버 요청 실패: ${response.exceptionOrNull()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo> {
        return commentRemoteDataSource.fetchCommentOfferingInfo(
            offeringId,
        ).mapCatching { commentOfferingInfo ->
            commentOfferingInfo.toDomain()
        }
    }
    
    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit> {
        return commentRemoteDataSource.updateOfferingStatus(offeringId)
    }
}
