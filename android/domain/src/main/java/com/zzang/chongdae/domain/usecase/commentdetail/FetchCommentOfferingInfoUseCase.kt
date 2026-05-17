package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.domain.repository.CommentDetailRepository
import javax.inject.Inject

class FetchCommentOfferingInfoUseCase
    @Inject
    constructor(
        private val repository: CommentDetailRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = repository.fetchCommentOfferingInfo(offeringId)
    }
