package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.CommentsApiService
import com.zzang.chongdae.data.remote.api.OfferingsApiService
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import retrofit2.Response

class CommentRemoteDataSourceImpl(
    private val offeringsApiService: OfferingsApiService,
    private val commentsApiService: CommentsApiService,
) : CommentRemoteDataSource {
    override suspend fun getMeetings(offeringId: Long): Result<MeetingsResponse> {
        return runCatching {
            val response: Response<MeetingsResponse> = offeringsApiService.getMeetings(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("에러 발생: ${response.code()}")
            }
        }
    }

    override suspend fun saveComment(commentRequest: CommentRequest): Result<Unit> =
        runCatching {
            commentsApiService.postComment(commentRequest = commentRequest)
                .body() ?: throw IllegalStateException()
        }

    override suspend fun fetchComments(
        offeringId: Long,
        memberId: Long,
    ): Result<CommentsResponse> =
        runCatching {
            val response: Response<CommentsResponse> =
                commentsApiService.getComments(offeringId, memberId)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("에러 발생: ${response.code()}")
            }
        }
}
