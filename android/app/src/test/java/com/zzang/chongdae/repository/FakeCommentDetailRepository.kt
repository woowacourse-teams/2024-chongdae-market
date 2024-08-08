package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.util.TestFixture

class FakeCommentDetailRepository : CommentDetailRepository {
    private val comments: MutableList<Comment> = TestFixture.comments1

    override suspend fun fetchMeetings(offeringId: Long): Result<Meetings> {
        return Result.success(
            TestFixture.meetings,
        )
    }

    override suspend fun saveComment(
        memberId: Long,
        offeringId: Long,
        comment: String,
    ): Result<Unit> {
        comments.add(TestFixture.comment1)
        return Result.success(Unit)
    }

    override suspend fun fetchComments(
        offeringId: Long,
        memberId: Long,
    ): Result<List<Comment>> {
        return Result.success(
            comments.toList(),
        )
    }

    override suspend fun fetchCommentsWithRoom(
        offeringId: Long,
        memberId: Long,
    ): Result<List<Comment>> {
        return Result.success(
            comments.toList(),
        )
    }
}
