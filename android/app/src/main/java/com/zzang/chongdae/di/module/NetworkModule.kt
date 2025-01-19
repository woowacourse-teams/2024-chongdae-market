package com.zzang.chongdae.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.interceptor.TokenAuthenticator
import com.zzang.chongdae.data.remote.util.TokensCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        userPreferencesDataStore: UserPreferencesDataStore,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(TokensCookieJar(userPreferencesDataStore))
            .authenticator(tokenAuthenticator)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideOfferingApiService(retrofit: Retrofit): OfferingApiService = retrofit.create(OfferingApiService::class.java)

    @Provides
    @Singleton
    fun provideParticipationApiService(retrofit: Retrofit): ParticipationApiService = retrofit.create(ParticipationApiService::class.java)

    @Provides
    @Singleton
    fun provideCommentApiService(retrofit: Retrofit): CommentApiService = retrofit.create(CommentApiService::class.java)
}
