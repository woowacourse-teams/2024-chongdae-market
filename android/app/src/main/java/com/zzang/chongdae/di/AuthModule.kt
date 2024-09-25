package com.zzang.chongdae.di

import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.api.NetworkManager
import dagger.Binds
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedAuthRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedAuthDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedAuthApiService

abstract class AuthModule {
    @Binds
    @Singleton
    @SharedAuthRepository
    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    @SharedAuthDataSource
    abstract fun provideAuthDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    companion object {
        @Provides
        @Singleton
        @SharedAuthApiService
        fun provideAuthApiService() : AuthApiService {
            return NetworkManager.authService()
        }
    }
}