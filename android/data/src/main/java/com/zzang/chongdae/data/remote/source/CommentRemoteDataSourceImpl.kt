package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.network.safeApiCall
import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.data.remote.dto.response.comment.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.comment.UpdatedStatusResponse
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import javax.inject.Inject

class CommentRemoteDataSourceImpl
    @Inject
    constructor(
        private val service: com.zzang.chongdae.data.remote.api.CommentApiService,
    ) : CommentRemoteDataSource {
        override suspend fun saveComment(
            commentRequest: com.zzang.chongdae.data.remote.dto.request.CommentRequest,
        ): Result<Unit, DataError.Network> =
            safeApiCall {
                service.postComment(commentRequest)
            }

        override suspend fun fetchComments(
            offeringId: Long,
        ): Result<com.zzang.chongdae.data.remote.dto.response.comment.CommentsResponse, DataError.Network> =
            safeApiCall {
                service.getComments(offeringId)
            }

        override suspend fun fetchCommentOfferingInfo(
            offeringId: Long,
        ): Result<com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse, DataError.Network> =
            safeApiCall {
                service.getCommentOfferingInfo(offeringId)
            }

        override suspend fun updateOfferingStatus(
            offeringId: Long,
        ): Result<com.zzang.chongdae.data.remote.dto.response.comment.UpdatedStatusResponse, DataError.Network> =
            safeApiCall {
                service.patchOfferingStatus(offeringId)
            }
    }
