package com.zzang.chongdae.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.comment.Comment
import com.zzang.chongdae.domain.model.comment.CommentOfferingInfo
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.util.TestFixture

class FakeCommentDetailRepository : CommentDetailRepository {
    private val comments: MutableList<Comment> = TestFixture.comments
    private var commentOfferingInfo: CommentOfferingInfo = TestFixture.commentOfferingInfo

    init {
        comments.clear()
        comments.add(TestFixture.comment)
    }

    override suspend fun saveComment(
        offeringId: Long,
        comment: String,
    ): Result<Unit, DataError.Network> {
        comments.add(TestFixture.comment)
        return Result.Success(Unit)
    }

    override suspend fun fetchComments(offeringId: Long): Result<List<Comment>, DataError.Network> {
        return Result.Success(
            comments.toList(),
        )
    }

    override suspend fun fetchCommentOfferingInfo(offeringId: Long): Result<CommentOfferingInfo, DataError.Network> =
        Result.Success(
            commentOfferingInfo,
        )

    override suspend fun updateOfferingStatus(offeringId: Long): Result<Unit, DataError.Network> {
        commentOfferingInfo = TestFixture.commentOfferingInfo2
        return Result.Success(Unit)
    }
}
