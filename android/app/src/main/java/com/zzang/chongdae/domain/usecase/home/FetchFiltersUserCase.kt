package com.zzang.chongdae.domain.usecase.home

import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class FetchFiltersUserCase
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
    ) {
        suspend operator fun invoke() = offeringRepository.fetchFilters()
    }
