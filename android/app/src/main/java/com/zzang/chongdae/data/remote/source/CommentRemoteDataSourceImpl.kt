package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentOfferingInfoResponse
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import retrofit2.Response

class CommentRemoteDataSourceImpl(
    private val service: CommentApiService,
) : CommentRemoteDataSource {
    override suspend fun saveComment(commentRequest: CommentRequest): Result<Unit> =
        runCatching {
            val response = service.postComment(commentRequest)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("${ERROR_PREFIX}${response.code()}")
            }
        }

    override suspend fun fetchComments(offeringId: Long): Result<CommentsResponse> =
        runCatching {
            val response: Response<CommentsResponse> =
                service.getComments(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error("${ERROR_PREFIX}${response.code()}")
            }
        }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfoResponse> {
        return runCatching {
            val response: Response<CommentOfferingInfoResponse> =
                service.getCommentOfferingInfo(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error("${ERROR_PREFIX}${response.code()}")
            }
        }
    }

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit> {
        return runCatching {
            val response = service.patchOfferingStatus(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error("${ERROR_PREFIX}${response.code()}")
            }
        }
    }

    companion object {
        const val ERROR_PREFIX = "에러 발생: "
        const val ERROR_NULL_MESSAGE = "null"
    }
}
