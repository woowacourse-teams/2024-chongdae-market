package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnalyticsRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnalyticsDataSourceQualifier
