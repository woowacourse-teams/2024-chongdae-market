package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.util.TestFixture
import com.zzang.chongdae.util.TestFixture.commentOfferingInfo

class FakeCommentDetailRepository : CommentDetailRepository {
    private val comments: MutableList<Comment> = TestFixture.comments
    private var commentOfferingInfo: CommentOfferingInfo = TestFixture.commentOfferingInfo

    override suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit> {
        comments.add(TestFixture.comment)
        return Result.success(Unit)
    }

    override suspend fun fetchComments(offeringId: Long): Result<List<Comment>> {
        return Result.success(
            comments.toList(),
        )
    }

    override suspend fun fetchCommentsWithRoom(offeringId: Long): Result<List<Comment>> {
        return Result.success(
            comments.toList(),
        )
    }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo> =
        Result.success(
            commentOfferingInfo,
        )

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit> {
        commentOfferingInfo = TestFixture.commentOfferingInfo2
        return Result.success(Unit)
    }
}
