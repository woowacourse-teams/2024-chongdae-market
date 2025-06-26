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

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentDetailDependencyModule {
    @Binds
    @Singleton
    abstract fun bindCommentDetailRepository(
        impl: CommentDetailRepositoryImpl
    ): CommentDetailRepository

    @Binds
    @Singleton
    abstract fun bindCommentRemoteDataSource(
        impl: CommentRemoteDataSourceImpl
    ): CommentRemoteDataSource
}
