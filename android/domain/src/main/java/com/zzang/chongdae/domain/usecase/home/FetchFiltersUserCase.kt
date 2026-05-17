package com.zzang.chongdae.domain.usecase.home

import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class FetchFiltersUserCase
    @Inject
    constructor(
        private val offeringRepository: OfferingRepository,
    ) {
        suspend operator fun invoke() = offeringRepository.fetchFilters()
    }
