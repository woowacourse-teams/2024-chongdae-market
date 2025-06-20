package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadImageFileUseCaseImpl
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) : UploadImageFileUseCase {
        override suspend fun invoke(
            multipartBody: MultipartBody.Part,
        ): com.zzang.chongdae.common.handler.Result<ProductUrl, com.zzang.chongdae.common.handler.DataError.Network> {
            return when (val result = offeringRepository.saveProductImageS3(multipartBody)) {
                is com.zzang.chongdae.common.handler.Result.Success -> com.zzang.chongdae.common.handler.Result.Success(result.data)
                is com.zzang.chongdae.common.handler.Result.Error -> {
                    when (result.error) {
                        com.zzang.chongdae.common.handler.DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is com.zzang.chongdae.common.handler.Result.Success -> invoke(multipartBody)
                                is com.zzang.chongdae.common.handler.Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
