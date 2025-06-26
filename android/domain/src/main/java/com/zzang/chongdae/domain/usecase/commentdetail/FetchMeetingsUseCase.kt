package com.zzang.chongdae.domain.usecase.commentdetail

import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class FetchMeetingsUseCase
    @Inject
    constructor(
        private val repository: OfferingRepository,
    ) {
        suspend operator fun invoke(offeringId: Long) = repository.fetchMeetings(offeringId)
    }
