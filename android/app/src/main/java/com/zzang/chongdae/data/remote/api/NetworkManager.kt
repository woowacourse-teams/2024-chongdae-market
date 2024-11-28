package com.zzang.chongdae.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkManager {
    private var instance: Retrofit? = null

    private const val BASE_URL = "http://fromitive.iptime.org"

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
                    .baseUrl(BASE_URL)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .client(
                        OkHttpClient.Builder()
                            .build(),
                    )
                    .build()
        }
        return instance!!
    }

    fun service(): GroupPurchaseApiService = getRetrofit().create(GroupPurchaseApiService::class.java)
}
