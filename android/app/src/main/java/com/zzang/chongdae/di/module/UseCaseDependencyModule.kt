package com.zzang.chongdae.di.module

import com.zzang.chongdae.di.annotations.CheckAlreadyLoggedInUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostLoginUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostOfferingUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostProductImageOgUseCaseQualifier
import com.zzang.chongdae.di.annotations.UploadImageFileUseCaseQualifier
import com.zzang.chongdae.domain.usecase.login.CheckIfAlreadyLoggedInUseCase
import com.zzang.chongdae.domain.usecase.login.CheckIfAlreadyLoggedInUseCaseImpl
import com.zzang.chongdae.domain.usecase.login.PostLoginUseCase
import com.zzang.chongdae.domain.usecase.login.PostLoginUseCaseImpl
import com.zzang.chongdae.domain.usecase.write.PostOfferingUseCase
import com.zzang.chongdae.domain.usecase.write.PostOfferingUseCaseImpl
import com.zzang.chongdae.domain.usecase.write.PostProductImageOgUseCase
import com.zzang.chongdae.domain.usecase.write.PostProductImageOgUseCaseImpl
import com.zzang.chongdae.domain.usecase.write.UploadImageFileUseCase
import com.zzang.chongdae.domain.usecase.write.UploadImageFileUseCaseImpl
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

    @Binds
    @Singleton
    @UploadImageFileUseCaseQualifier
    abstract fun provideUploadImageFileUseCase(impl: UploadImageFileUseCaseImpl): UploadImageFileUseCase

    @Binds
    @Singleton
    @PostProductImageOgUseCaseQualifier
    abstract fun providePostProductImageOgUseCase(impl: PostProductImageOgUseCaseImpl): PostProductImageOgUseCase
}
