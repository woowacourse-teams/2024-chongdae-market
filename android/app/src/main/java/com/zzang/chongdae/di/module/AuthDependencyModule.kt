package com.zzang.chongdae.di.module

import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.di.annotations.AuthApiServiceQualifier
import com.zzang.chongdae.di.annotations.AuthDataSourceQualifier
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthDependencyModule {
    @Binds
    @Singleton
    @AuthRepositoryQualifier
    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    @AuthDataSourceQualifier
    abstract fun provideAuthDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    companion object {
        @Provides
        @Singleton
        @AuthApiServiceQualifier
        fun provideAuthApiService(): AuthApiService {
            return NetworkManager.authService()
        }
    }
}
