package com.zzang.chongdae.data.source.comment

import com.zzang.chongdae.local.model.CommentEntity

interface CommentLocalDataSource {
    suspend fun insertComments(comments: List<CommentEntity>)

    suspend fun getLatestCommentId(offeringId: Long): Long?

    suspend fun getCommentsByOfferingId(offeringId: Long): List<CommentEntity>
}
