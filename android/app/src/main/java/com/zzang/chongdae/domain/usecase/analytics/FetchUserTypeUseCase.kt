package com.zzang.chongdae.domain.usecase.analytics

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AnalyticsRepositoryQualifier
import com.zzang.chongdae.domain.model.analytics.UserType
import com.zzang.chongdae.domain.repository.AnalyticsRepository
import javax.inject.Inject

class FetchUserTypeUseCase
    @Inject
    constructor(
        @AnalyticsRepositoryQualifier private val analyticsRepository: AnalyticsRepository,
    ) {
        suspend operator fun invoke(): Result<UserType, DataError.Network> {
            return analyticsRepository.fetchUserType()
        }
    }
