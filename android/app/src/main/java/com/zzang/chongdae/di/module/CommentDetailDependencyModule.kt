package com.zzang.chongdae.di.module

import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.local.source.CommentLocalDataSourceImpl
import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.CommentRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.CommentDetailRepositoryImpl
import com.zzang.chongdae.data.source.comment.CommentRemoteDataSource
import com.zzang.chongdae.di.annotations.AuthApiServiceQualifier
import com.zzang.chongdae.di.annotations.AuthDataSourceQualifier
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.CommentDetailApiServiceQualifier
import com.zzang.chongdae.di.annotations.CommentDetailDataSourceQualifier
import com.zzang.chongdae.di.annotations.CommentDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
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

    companion object {
        @Provides
        @Singleton
        @CommentDetailApiServiceQualifier
        fun provideCommentDetailApiService(): CommentApiService {
            return NetworkManager.commentService()
        }
    }
}
