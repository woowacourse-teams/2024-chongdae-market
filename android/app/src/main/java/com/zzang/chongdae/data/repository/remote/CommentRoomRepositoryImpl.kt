package com.zzang.chongdae.data.repository.remote

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.remote.source.CommentRoomDataSource
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomRepository

class CommentRoomRepositoryImpl(
    private val commentRoomDataSource: CommentRoomDataSource,
) : CommentRoomRepository {
    override suspend fun fetchCommentRoom(memberId: Long): Result<List<CommentRoom>> {
        return commentRoomDataSource.fetchCommentRooms(memberId).mapCatching {
            it.commentRoom.map { it.toDomain() }
        }
    }
}
