package com.zzang.chongdae.data.remote.util

import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.data.local.source.UserPreferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class TokensCookieJar(private val userPreferencesDataStore: UserPreferencesDataStore) : CookieJar {
    private val cookies: MutableMap<String, List<Cookie>> = mutableMapOf()
    private val urlHost = BuildConfig.BASE_URL.removePrefix(URL_PREFIX).removePrefix(URL_PREFIX)
        .substringBefore("/")

    init {
        loadTokensFromDataStore()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies[url.host] ?: emptyList()
    }

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>,
    ) {
        this.cookies[url.host] = cookies
        saveTokensToDataStore(cookies)
    }

    private fun saveTokensToDataStore(cookies: List<Cookie>) {
        val accessToken = cookies.first { it.name == ACCESS_TOKEN_NAME }.value
        val refreshToken = cookies.first { it.name == REFRESH_TOKEN_NAME }.value
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesDataStore.saveTokens(accessToken, refreshToken)
        }
    }

    private fun loadTokensFromDataStore() {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = userPreferencesDataStore.accessTokenFlow.first() ?: return@launch
            val refreshToken = userPreferencesDataStore.refreshTokenFlow.first() ?: return@launch
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
        private const val URL_PREFIX = "http://"
    }
}
