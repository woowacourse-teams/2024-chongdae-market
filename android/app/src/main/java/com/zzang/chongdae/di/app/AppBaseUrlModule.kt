package com.zzang.chongdae.di.app

import com.zzang.chongdae.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppBaseUrlModule {
    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(): String = BuildConfig.BASE_URL
}
