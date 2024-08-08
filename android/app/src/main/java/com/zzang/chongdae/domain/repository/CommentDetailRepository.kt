package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.Meetings

interface CommentDetailRepository {
    suspend fun fetchMeetings(offeringId: Long): Result<Meetings>

    suspend fun saveComment(
        memberId: Long,
        offeringId: Long,
        comment: String,
    ): Result<Unit>

    suspend fun fetchComments(
        offeringId: Long,
        memberId: Long,
    ): Result<List<Comment>>

    suspend fun fetchCommentsWithRoom(
        offeringId: Long,
        memberId: Long,
    ): Result<List<Comment>>
}
