package com.zzang.chongdae.domain.usecase.comment

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.commentroom.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import javax.inject.Inject

class UpdateCommentRoomsUseCase
    @Inject
    constructor(
        private val commentRoomsRepository: CommentRoomsRepository,
    ) {
        suspend operator fun invoke(): Result<List<CommentRoom>, DataError.Network> {
            return commentRoomsRepository.fetchCommentRooms()
        }
    }
