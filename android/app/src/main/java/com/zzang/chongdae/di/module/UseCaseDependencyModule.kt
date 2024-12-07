package com.zzang.chongdae.di.module

import com.zzang.chongdae.di.annotations.CheckAlreadyLoggedInUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostLoginUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostOfferingUseCaseQualifier
import com.zzang.chongdae.domain.usecase.login.CheckIfAlreadyLoggedInUseCase
import com.zzang.chongdae.domain.usecase.login.CheckIfAlreadyLoggedInUseCaseImpl
import com.zzang.chongdae.domain.usecase.login.PostLoginUseCase
import com.zzang.chongdae.domain.usecase.login.PostLoginUseCaseImpl
import com.zzang.chongdae.domain.usecase.write.PostOfferingUseCase
import com.zzang.chongdae.domain.usecase.write.PostOfferingUseCaseImpl
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
