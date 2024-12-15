package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CheckAlreadyLoggedInUseCaseQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PostLoginUseCaseQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PostOfferingUseCaseQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UploadImageFileUseCaseQualifier
