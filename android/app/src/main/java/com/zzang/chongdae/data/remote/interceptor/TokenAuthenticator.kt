package com.zzang.chongdae.data.remote.interceptor

import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.usecase.auth.RefreshTokenUseCase
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator
    @Inject
    constructor(
        private val refreshTokenUseCase: Lazy<RefreshTokenUseCase>,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (response.request.header(AUTHORIZATION_HEADER) != null) {
                return null
            }

            val newToken =
                runBlocking {
                    when (val result = refreshTokenUseCase.get()()) {
                        is Result.Success -> result.data
                        else -> null
                    }
                } ?: return null

            return response.request.newBuilder()
                .header(AUTHORIZATION_HEADER, "$TOKEN_PREFIX$newToken")
                .build()
        }

        companion object {
            private const val AUTHORIZATION_HEADER = "Authorization"
            private const val TOKEN_PREFIX = "Bearer "
        }
    }
