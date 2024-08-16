package com.zzang.chongdae.data.source.comment

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.CommentOfferingInfoResponse

interface CommentRemoteDataSource {
    suspend fun saveComment(commentRequest: CommentRequest): Result<Unit>

    suspend fun fetchComments(offeringId: Long): Result<CommentsResponse>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfoResponse>
}
