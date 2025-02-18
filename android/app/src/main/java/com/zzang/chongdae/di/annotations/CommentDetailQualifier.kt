package com.zzang.chongdae.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommentDetailRepositoryQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommentDetailDataSourceQualifier
