package com.zzang.chongdae.data.source.comment

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.data.remote.dto.response.comment.CommentsResponse

interface CommentRemoteDataSource {
    suspend fun saveComment(commentRequest: CommentRequest): Result<Unit>

    suspend fun fetchComments(offeringId: Long): Result<CommentsResponse>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfoResponse>

    suspend fun updateOfferingStatus(offeringId: Long): Result<Unit>
}
