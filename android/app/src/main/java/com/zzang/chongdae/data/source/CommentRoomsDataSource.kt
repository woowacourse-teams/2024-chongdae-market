package com.zzang.chongdae.data.source

import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.dto.response.commentroom.CommentRoomsResponse

interface CommentRoomsDataSource {
    suspend fun fetchCommentRooms(): Result<CommentRoomsResponse, DataError.Network>
}
