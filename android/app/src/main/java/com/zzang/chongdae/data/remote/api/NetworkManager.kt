package com.zzang.chongdae.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.data.remote.util.SimpleCookieJar
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkManager {
    private var instance: Retrofit? = null

    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    private fun getRetrofit(): Retrofit {
        if (instance == null) {
            val contentType = "application/json".toMediaType()
            instance =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .client(
                        OkHttpClient.Builder()
                            .cookieJar(SimpleCookieJar())
                            .build(),
                    )
                    .build()
        }
        return instance!!
    }

    fun offeringService(): OfferingApiService = getRetrofit().create(OfferingApiService::class.java)

    fun participationService(): ParticipationApiService = getRetrofit().create(ParticipationApiService::class.java)

    fun commentService(): CommentApiService = getRetrofit().create(CommentApiService::class.java)

    fun authService(): AuthApiService = getRetrofit().create(AuthApiService::class.java)
}
