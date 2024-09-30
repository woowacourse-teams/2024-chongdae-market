package com.zzang.chongdae.data.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.mapper.toDomain
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.di.annotations.CommentRoomsDataSourceQualifier
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import javax.inject.Inject

class CommentRoomsRepositoryImpl
    @Inject
    constructor(
        @CommentRoomsDataSourceQualifier private val commentRoomsDataSource: CommentRoomsDataSource,
    ) : CommentRoomsRepository {
        override suspend fun fetchCommentRooms(): Result<List<CommentRoom>, DataError.Network> {
            return commentRoomsDataSource.fetchCommentRooms().map {
                it.commentRoom.map { it.toDomain() }
            }
        }
    }
