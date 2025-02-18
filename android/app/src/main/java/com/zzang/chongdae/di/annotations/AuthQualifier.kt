package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthDataSourceQualifier
