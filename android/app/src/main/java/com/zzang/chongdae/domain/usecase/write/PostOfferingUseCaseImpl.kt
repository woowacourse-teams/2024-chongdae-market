package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.OfferingWrite
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostOfferingUseCaseImpl @Inject constructor(
    @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
    @AuthRepositoryQualifier private val authRepository: AuthRepository,
) : PostOfferingUseCase {
    override suspend fun invoke(offeringWrite: OfferingWrite): Result<Unit, DataError.Network> {
        return when (val result = offeringRepository.saveOffering(offeringWrite)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.UNAUTHORIZED -> {
                        when (authRepository.saveRefresh()) {
                            is Result.Success -> invoke(offeringWrite)
                            is Result.Error -> result
                        }
                    }
                    else -> result
                }
            }
        }
    }
}