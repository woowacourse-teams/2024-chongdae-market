package com.zzang.chongdae.di

import com.zzang.chongdae.auth.api.AuthApiService
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSource
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedCommentRoomsRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedCommentRoomsDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedCommentRoomsApiService

@InstallIn(SingletonComponent::class)
@Module
abstract class ChongdaeAppModule {
    @Binds
    @Singleton
    @SharedCommentRoomsRepository
    abstract fun provideCommentRoomsRepository(impl: CommentRoomsRepositoryImpl): CommentRoomsRepository

    @Binds
    @Singleton
    @SharedCommentRoomsDataSource
    abstract fun provideCommentRoomsDataSource(impl: CommentRoomsDataSourceImpl): CommentRoomsDataSource

    companion object {
        @Provides
        @Singleton
        @SharedCommentRoomsApiService
        fun provideCommentRoomsService() : CommentApiService {
            return NetworkManager.commentService()
        }
    }
}
