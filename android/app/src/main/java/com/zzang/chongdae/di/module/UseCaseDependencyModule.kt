package com.zzang.chongdae.di.module

import com.zzang.chongdae.di.annotations.PostLoginUseCaseQualifier
import com.zzang.chongdae.presentation.view.login.usecase.PostLoginUseCase
import com.zzang.chongdae.presentation.view.login.usecase.PostLoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCaseDependencyModule {
    @Binds
    @Singleton
    @PostLoginUseCaseQualifier
    abstract fun provideLoginUseCase(impl: PostLoginUseCaseImpl): PostLoginUseCase
}
