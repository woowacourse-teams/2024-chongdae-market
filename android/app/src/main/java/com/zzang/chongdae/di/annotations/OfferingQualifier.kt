package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingDataSourceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfferingApiServiceQualifier
