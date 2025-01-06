package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingDetailRepositoryQualifier
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import javax.inject.Inject

class FetchOfferingDetailUseCaseImpl
    @Inject
    constructor(
        @OfferingDetailRepositoryQualifier private val offeringDetailRepository: OfferingDetailRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) : FetchOfferingDetailUseCase {
        override suspend fun invoke(offeringId: Long): Result<OfferingDetail, DataError.Network> {
            return when (val result = offeringDetailRepository.fetchOfferingDetail(offeringId)) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is Result.Success -> invoke(offeringId)
                                is Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
