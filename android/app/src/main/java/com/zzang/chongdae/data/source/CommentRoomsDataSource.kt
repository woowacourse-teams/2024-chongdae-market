package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomsResponse
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface CommentRoomsDataSource {
    suspend fun fetchCommentRooms(): Result<CommentRoomsResponse, DataError.Network>
}
