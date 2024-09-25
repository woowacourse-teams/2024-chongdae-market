package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.response.commentroom.CommentRoomsResponse

interface CommentRoomsDataSource {
    suspend fun fetchCommentRooms(): Result<CommentRoomsResponse, DataError.Network>
}
