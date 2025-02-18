package com.zzang.chongdae.domain.usecase.login

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import javax.inject.Inject

class PostLoginUseCaseImpl
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : PostLoginUseCase {
        override suspend fun invoke(
            accessToken: String,
            fcmToken: String,
        ): Result<Unit, DataError.Network> {
            return when (val result = authRepository.postLogin(accessToken, fcmToken)) {
                is Result.Success -> {
                    userPreferencesDataStore.saveMember(result.data.memberId, result.data.nickName)
                    userPreferencesDataStore.saveFcmToken(fcmToken)
                    Result.Success(Unit)
                }

                is Result.Error -> result
            }
        }
    }
