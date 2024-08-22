package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.util.TestFixture

class FakeCommentDetailRepository : CommentDetailRepository {
    private val comments: MutableList<Comment> = TestFixture.comments
    private var commentOfferingInfo: CommentOfferingInfo = TestFixture.commentOfferingInfo

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
