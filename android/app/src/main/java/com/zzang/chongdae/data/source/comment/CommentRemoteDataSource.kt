package com.zzang.chongdae.data.source.comment

import com.zzang.chongdae.data.remote.dto.request.CommentRequest
import com.zzang.chongdae.data.remote.dto.response.CommentsResponse
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse

interface CommentRemoteDataSource {
    suspend fun getMeetings(offeringId: Long): Result<MeetingsResponse>

    suspend fun saveComment(commentRequest: CommentRequest): Result<Unit>

    suspend fun fetchComments(offeringId: Long): Result<CommentsResponse>
}
