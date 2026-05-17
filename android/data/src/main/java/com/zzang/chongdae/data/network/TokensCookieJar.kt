package com.zzang.chongdae.data.network

import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Named

class TokensCookieJar
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        @Named("BaseUrl") baseUrl: String,
    ) : CookieJar {
        private val cookies: MutableMap<String, List<Cookie>> = mutableMapOf()
        private val urlHost =
            baseUrl.removePrefix(URL_PREFIX_HTTP).removePrefix(URL_PREFIX_HTTPS)
                .substringBefore("/")

        init {
            loadTokensFromRepository()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies[url.host] ?: emptyList()
        }

        override fun saveFromResponse(
            url: HttpUrl,
            cookies: List<Cookie>,
        ) {
            this.cookies[url.host] = cookies
            saveTokensToRepository(cookies)
        }

        private fun saveTokensToRepository(cookies: List<Cookie>) {
            val accessToken = cookies.firstOrNull { it.name == ACCESS_TOKEN_NAME }?.value ?: return
            val refreshToken = cookies.firstOrNull { it.name == REFRESH_TOKEN_NAME }?.value ?: return

            CoroutineScope(Dispatchers.IO).launch {
                userPreferencesRepository.saveAccountTokens(accessToken, refreshToken)
            }
        }

        private fun loadTokensFromRepository() {
            CoroutineScope(Dispatchers.IO).launch {
                val accessToken = userPreferencesRepository.accessTokenFlow.first() ?: return@launch
                val refreshToken = userPreferencesRepository.refreshTokenFlow.first() ?: return@launch

                val accessTokenCookie = makeTokenCookie(ACCESS_TOKEN_NAME, accessToken)
                val refreshTokenCookie = makeTokenCookie(REFRESH_TOKEN_NAME, refreshToken)
                cookies[urlHost] = listOf(accessTokenCookie, refreshTokenCookie)
            }
        }

        private fun makeTokenCookie(
            tokenName: String,
            tokenValue: String,
        ): Cookie {
            return Cookie.Builder()
                .name(tokenName)
                .value(tokenValue)
                .hostOnlyDomain(urlHost)
                .httpOnly()
                .build()
        }

        companion object {
            private const val ACCESS_TOKEN_NAME = "access_token"
            private const val REFRESH_TOKEN_NAME = "refresh_token"
            private const val URL_PREFIX_HTTP = "http://"
            private const val URL_PREFIX_HTTPS = "https://"
        }
    }
