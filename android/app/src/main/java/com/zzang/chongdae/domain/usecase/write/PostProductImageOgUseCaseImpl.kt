package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import javax.inject.Inject

class PostProductImageOgUseCaseImpl
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) : PostProductImageOgUseCase {
        override suspend fun invoke(
            productUrl: String,
        ): com.zzang.chongdae.common.handler.Result<ProductUrl, com.zzang.chongdae.common.handler.DataError.Network> {
            return when (val result = offeringRepository.saveProductImageOg(productUrl)) {
                is com.zzang.chongdae.common.handler.Result.Success -> com.zzang.chongdae.common.handler.Result.Success(result.data)
                is com.zzang.chongdae.common.handler.Result.Error -> {
                    when (result.error) {
                        com.zzang.chongdae.common.handler.DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is com.zzang.chongdae.common.handler.Result.Success -> invoke(productUrl)
                                is com.zzang.chongdae.common.handler.Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
