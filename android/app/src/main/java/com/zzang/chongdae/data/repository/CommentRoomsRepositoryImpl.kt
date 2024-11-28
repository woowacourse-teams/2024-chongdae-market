package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository

class CommentRoomsRepositoryImpl(
    private val commentRoomsDataSource: CommentRoomsDataSource,
) : CommentRoomsRepository {
    override suspend fun fetchCommentRooms(): Result<List<CommentRoom>> {
        return commentRoomsDataSource.fetchCommentRooms().mapCatching {
            it.commentRoom.map { it.toDomain() }
        }
    }
}
