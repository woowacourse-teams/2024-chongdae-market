package com.zzang.chongdae.domain.usecase.comment

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.CommentRoom

interface UpdateCommentRoomsUseCase {
    suspend operator fun invoke(): Result<List<CommentRoom>, DataError.Network>
}
