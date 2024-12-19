package com.zzang.chongdae.domain.usecase.offeringmodify

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostOfferingModifyUseCaseImpl
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) : PostOfferingModifyUseCase {
        override suspend fun invoke(
            offeringId: Long,
            offeringModifyDomainRequest: OfferingModifyDomainRequest,
        ): Result<Unit, DataError.Network> {
            return when (
                val result =
                    offeringRepository.patchOffering(offeringId, offeringModifyDomainRequest)
            ) {
                is Result.Success -> Result.Success(Unit)
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is Result.Success -> invoke(offeringId, offeringModifyDomainRequest)
                                is Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
