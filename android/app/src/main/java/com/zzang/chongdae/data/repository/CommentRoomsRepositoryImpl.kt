package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.local.mapper.toDomain
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class CommentRoomsRepositoryImpl(
    private val commentRoomsDataSource: CommentRoomsDataSource,
) : CommentRoomsRepository {
    override suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network> {
        return commentRoomsDataSource.fetchCommentRooms().map {
            it.commentRoom.map { it.toDomain() }
        }
    }
}
