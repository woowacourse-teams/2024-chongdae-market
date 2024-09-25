package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommentRoomsRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommentRoomsDataSourceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommentRoomsApiServiceQualifier
