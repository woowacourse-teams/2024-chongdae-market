package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.dto.request.CommentRequest
import com.zzang.chongdae.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.remote.dto.response.comment.CommentsResponse
import com.zzang.chongdae.remote.dto.response.comment.UpdatedStatusResponse
import com.zzang.chongdae.remote.util.safeApiCall

class CommentRemoteDataSourceImpl(
    private val service: com.zzang.chongdae.remote.api.CommentApiService,
) : CommentRemoteDataSource {
    override suspend fun saveComment(commentRequest: CommentRequest): Result<Unit, DataError.Network> =
        safeApiCall {
            service.postComment(commentRequest)
        }

    override suspend fun fetchComments(offeringId: Long): Result<CommentsResponse, DataError.Network> =
        safeApiCall {
            service.getComments(offeringId)
        }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfoResponse, DataError.Network> =
        safeApiCall {
            service.getCommentOfferingInfo(offeringId)
        }

    override suspend fun updateOfferingStatus(offeringId: Long): Result<UpdatedStatusResponse, DataError.Network> =
        safeApiCall {
            service.patchOfferingStatus(offeringId)
        }
}
