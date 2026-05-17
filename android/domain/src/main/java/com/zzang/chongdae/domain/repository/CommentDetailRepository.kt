package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.comment.Comment
import com.zzang.chongdae.domain.model.comment.CommentOfferingInfo

interface CommentDetailRepository {
    suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit, DataError.Network>

    suspend fun fetchComments(offeringId: Long): Result<List<Comment>, DataError.Network>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo, DataError.Network>

    suspend fun updateOfferingStatus(offeringId: Long): Result<Unit, DataError.Network>
}
