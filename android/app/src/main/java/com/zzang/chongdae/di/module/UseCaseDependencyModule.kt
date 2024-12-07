package com.zzang.chongdae.di.module

import com.zzang.chongdae.di.annotations.CheckAlreadyLoggedInUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostLoginUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostOfferingUseCaseQualifier
import com.zzang.chongdae.presentation.view.login.usecase.CheckIfAlreadyLoggedInUseCase
import com.zzang.chongdae.presentation.view.login.usecase.CheckIfAlreadyLoggedInUseCaseImpl
import com.zzang.chongdae.presentation.view.login.usecase.PostLoginUseCase
import com.zzang.chongdae.presentation.view.login.usecase.PostLoginUseCaseImpl
import com.zzang.chongdae.presentation.view.write.usecase.PostOfferingUseCase
import com.zzang.chongdae.presentation.view.write.usecase.PostOfferingUseCaseImpl
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
    @CheckAlreadyLoggedInUseCaseQualifier
    abstract fun provideCheckIfAlreadyLoggedInUseCase(impl: CheckIfAlreadyLoggedInUseCaseImpl): CheckIfAlreadyLoggedInUseCase

    @Binds
    @Singleton
    @PostLoginUseCaseQualifier
    abstract fun providePostLoginUseCase(impl: PostLoginUseCaseImpl): PostLoginUseCase

    @Binds
    @Singleton
    @PostOfferingUseCaseQualifier
    abstract fun providePostOfferingUseCase(impl: PostOfferingUseCaseImpl): PostOfferingUseCase
}
