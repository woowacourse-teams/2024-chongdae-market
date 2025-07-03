package com.zzang.chongdae.di.module

import com.zzang.chongdae.auth.data.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.data.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.data.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthDependencyModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideAuthDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}
