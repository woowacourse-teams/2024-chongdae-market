package com.zzang.chongdae.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zzang.chongdae.BuildConfig
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
                            .build(),
                    )
                    .build()
        }
        return instance!!
    }

    fun offeringsService(): OfferingsApiService = getRetrofit().create(OfferingsApiService::class.java)

    fun offeringDetailService(): OfferingDetailApiService = getRetrofit().create(OfferingDetailApiService::class.java)

    fun commentRoomService(): CommentRoomApiService = getRetrofit().create(CommentRoomApiService::class.java)
}
