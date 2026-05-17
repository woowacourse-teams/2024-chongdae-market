package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.domain.repository.CommentDetailRepository
import javax.inject.Inject

class SaveCommentUseCase
    @Inject
    constructor(
        private val repository: CommentDetailRepository,
    ) {
        suspend operator fun invoke(
            offeringId: Long,
            content: String,
        ) = repository.saveComment(offeringId, content)
    }
