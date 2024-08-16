package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.model.Meetings

interface CommentDetailRepository {
    suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit>

    suspend fun fetchComments(offeringId: Long): Result<List<Comment>>

    suspend fun fetchCommentsWithRoom(offeringId: Long): Result<List<Comment>>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo>

    suspend fun updateOfferingStatus(offeringId: Long): Result<Unit>
}
