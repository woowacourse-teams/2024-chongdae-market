package com.zzang.chongdae.data.source.comment

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.data.remote.dto.response.comment.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.comment.UpdatedStatusResponse

interface CommentRemoteDataSource {
    suspend fun saveComment(commentRequest: CommentRequest): Result<Unit, DataError.Network>

    suspend fun fetchComments(offeringId: Long): Result<CommentsResponse, DataError.Network>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfoResponse, DataError.Network>

    suspend fun updateOfferingStatus(offeringId: Long): Result<UpdatedStatusResponse, DataError.Network>
}
