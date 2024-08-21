package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface CommentDetailRepository {
    suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit, DataError.Network>

    suspend fun fetchComments(offeringId: Long): Result<List<Comment>, DataError.Network>

    suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo, DataError.Network>

    suspend fun updateOfferingStatus(offeringId: Long): Result<Unit, DataError.Network>
}
