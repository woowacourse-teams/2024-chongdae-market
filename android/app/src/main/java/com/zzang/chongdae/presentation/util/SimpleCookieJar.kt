package com.zzang.chongdae.presentation.util

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SimpleCookieJar : CookieJar {
    private val cookieStore: MutableMap<String, List<Cookie>> = mutableMapOf()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>,
    ) {
        cookieStore[url.host] = cookies
    }
}
