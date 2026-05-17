package com.zzang.chongdae.auth.domain.usecase

import com.zzang.chongdae.auth.domain.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class PostLoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(
            accessToken: String,
            fcmToken: String,
        ): Result<Unit, DataError.Network> {
            return when (val result = authRepository.postLogin(accessToken, fcmToken)) {
                is Result.Success -> {
                    val member = result.data
                    userPreferencesRepository.saveMember(
                        memberId = member.memberId,
                        nickname = member.nickName,
                    )
                    userPreferencesRepository.saveFcmToken(fcmToken)
                    Result.Success(Unit)
                }

                is Result.Error -> result
            }
        }
    }
