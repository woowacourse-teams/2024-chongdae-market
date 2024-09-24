package com.zzang.chongdae.local.source

import com.zzang.chongdae.data.source.comment.CommentLocalDataSource
import com.zzang.chongdae.local.dao.CommentDao
import com.zzang.chongdae.local.model.CommentEntity

class CommentLocalDataSourceImpl(private val commentDao: CommentDao) : CommentLocalDataSource {
    override suspend fun insertComments(comments: List<CommentEntity>) {
        commentDao.insertAll(comments)
    }

    override suspend fun getLatestCommentId(offeringId: Long): Long? {
        return commentDao.getLastCommentIdByOfferingId(offeringId)
    }

    override suspend fun getCommentsByOfferingId(offeringId: Long): List<CommentEntity> {
        return commentDao.getCommentsByOfferingId(offeringId)
    }
}
