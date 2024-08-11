package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import retrofit2.Response

class CommentRemoteDataSourceImpl(
    private val offeringApiService: OfferingApiService,
    private val commentApiService: CommentApiService,
) : CommentRemoteDataSource {
    override suspend fun getMeetings(offeringId: Long): Result<MeetingsResponse> {
        return runCatching {
            val response: Response<MeetingsResponse> = offeringApiService.getMeetings(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("${response.code()}")
            }
        }
    }

    override suspend fun saveComment(commentRequest: CommentRequest): Result<Unit> =
        runCatching {
            val response = commentApiService.postComment(commentRequest)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else{
                error("${response.code()}")
            }
        }

    override suspend fun fetchComments(
        offeringId: Long,
    ): Result<CommentsResponse> =
        runCatching {
            val response: Response<CommentsResponse> =
                commentApiService.getComments(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("${response.code()}")
            }
        }
}
