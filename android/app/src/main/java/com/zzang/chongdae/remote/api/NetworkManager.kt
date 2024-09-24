package com.zzang.chongdae.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.ChongdaeApp.Companion.dataStore
import com.zzang.chongdae.local.source.UserPreferencesDataStore
import com.zzang.chongdae.remote.util.TokensCookieJar
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
        val userDataStore = UserPreferencesDataStore(ChongdaeApp.chongdaeApplicationContext.dataStore)
        if (com.zzang.chongdae.remote.api.NetworkManager.instance == null) {
            val contentType = "application/json".toMediaType()
            com.zzang.chongdae.remote.api.NetworkManager.instance =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(com.zzang.chongdae.remote.api.NetworkManager.json.asConverterFactory(contentType))
                    .client(
                        OkHttpClient.Builder()
                            .cookieJar(TokensCookieJar(userDataStore))
                            .build(),
                    )
                    .build()
        }
        return com.zzang.chongdae.remote.api.NetworkManager.instance!!
    }

    fun offeringService(): com.zzang.chongdae.remote.api.OfferingApiService =
        com.zzang.chongdae.remote.api.NetworkManager.getRetrofit()
            .create(com.zzang.chongdae.remote.api.OfferingApiService::class.java)

    fun participationService(): com.zzang.chongdae.remote.api.ParticipationApiService =
        com.zzang.chongdae.remote.api.NetworkManager.getRetrofit()
            .create(com.zzang.chongdae.remote.api.ParticipationApiService::class.java)

    fun commentService(): com.zzang.chongdae.remote.api.CommentApiService =
        com.zzang.chongdae.remote.api.NetworkManager.getRetrofit()
            .create(com.zzang.chongdae.remote.api.CommentApiService::class.java)

    fun authService(): com.zzang.chongdae.remote.api.AuthApiService =
        com.zzang.chongdae.remote.api.NetworkManager.getRetrofit()
            .create(com.zzang.chongdae.remote.api.AuthApiService::class.java)
}
