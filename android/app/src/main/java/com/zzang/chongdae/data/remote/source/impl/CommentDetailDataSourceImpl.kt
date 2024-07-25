package com.zzang.chongdae.data.remote.source.impl

import com.zzang.chongdae.data.remote.api.CommentDetailApiService
import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.remote.source.CommentDetailDataSource
import retrofit2.Response

class CommentDetailDataSourceImpl(
    private val service: CommentDetailApiService,
) : CommentDetailDataSource {
    override suspend fun getMeetings(offeringId: Long): Result<MeetingsResponse> {
        return runCatching {
            val response: Response<MeetingsResponse> = service.getMeetings(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error("에러 발생: null")
            } else {
                error("에러 발생: ${response.code()}")
            }
        }
    }

    override suspend fun saveComment(commentRequest: CommentRequest): Result<Unit> =
        runCatching {
            service.postComment(commentRequest = commentRequest)
                .body() ?: throw IllegalStateException()
        }
}
