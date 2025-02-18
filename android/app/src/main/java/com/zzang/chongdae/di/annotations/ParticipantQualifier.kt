package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ParticipantRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ParticipantDataSourceQualifier
