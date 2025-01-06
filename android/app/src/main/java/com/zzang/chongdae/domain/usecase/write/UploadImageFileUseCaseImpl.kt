package com.zzang.chongdae.domain.usecase.write

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.repository.OfferingRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadImageFileUseCaseImpl
    @Inject
    constructor(
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
    ) : UploadImageFileUseCase {
        override suspend fun invoke(multipartBody: MultipartBody.Part): Result<ProductUrl, DataError.Network> {
            return when (val result = offeringRepository.saveProductImageS3(multipartBody)) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is Result.Success -> invoke(multipartBody)
                                is Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
