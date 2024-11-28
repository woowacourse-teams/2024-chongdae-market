package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.util.TestFixture

class FakeCommentRoomsRepository : CommentRoomsRepository {
    override suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network> {
        return Result.Success(TestFixture.COMMENT_ROOMS_STUB)
    }
}
