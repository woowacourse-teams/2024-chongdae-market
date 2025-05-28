package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.di.annotations.CommentDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import javax.inject.Inject

class UpdateOfferingStatusUseCase
    @Inject
    constructor(
        @CommentDetailRepositoryQualifier private val repository: CommentDetailRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = repository.updateOfferingStatus(offeringId)
    }
