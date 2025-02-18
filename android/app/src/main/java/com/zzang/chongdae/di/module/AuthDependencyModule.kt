package com.zzang.chongdae.di.module

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.di.annotations.AuthDataSourceQualifier
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
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
    @AuthRepositoryQualifier
    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    @AuthDataSourceQualifier
    abstract fun provideAuthDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}
