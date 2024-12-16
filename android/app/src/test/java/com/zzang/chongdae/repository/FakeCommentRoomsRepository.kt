package com.zzang.chongdae.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.util.TestFixture

class FakeCommentRoomsRepository : CommentRoomsRepository {
    var isAccessTokenValid = true
    override suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network> {
        return when (isAccessTokenValid) {
            true -> Result.Success(TestFixture.COMMENT_ROOMS_STUB)
            false -> {
                isAccessTokenValid = true
                Result.Error("AccessToken 만료됨", DataError.Network.UNAUTHORIZED)
            }
        }
    }
}
