package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.source.CommentRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.CommentDetailRepositoryImpl
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import com.zzang.chongdae.di.annotations.CommentDetailDataSourceQualifier
import com.zzang.chongdae.di.annotations.CommentDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CommentDetailDependencyModule {
    @Binds
    @Singleton
    @CommentDetailRepositoryQualifier
    abstract fun provideCommentDetailRepository(impl: CommentDetailRepositoryImpl): CommentDetailRepository

    @Binds
    @Singleton
    @CommentDetailDataSourceQualifier
    abstract fun provideCommentDetailDataSource(impl: CommentRemoteDataSourceImpl): CommentRemoteDataSource
}
