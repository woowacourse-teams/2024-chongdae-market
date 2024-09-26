package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingRemoteDataSourceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingLocalDataSourceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingApiServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingDaoQualifier
