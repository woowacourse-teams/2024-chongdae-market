package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.api.CommentApiService
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.di.annotations.CommentRoomsApiServiceQualifier
import com.zzang.chongdae.di.annotations.CommentRoomsDataSourceQualifier
import com.zzang.chongdae.di.annotations.CommentRoomsRepositoryQualifier
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CommentRoomsDependencyModule {
    @Binds
    @Singleton
    @CommentRoomsRepositoryQualifier
    abstract fun provideCommentRoomsRepository(impl: CommentRoomsRepositoryImpl): CommentRoomsRepository

    @Binds
    @Singleton
    @CommentRoomsDataSourceQualifier
    abstract fun provideCommentRoomsDataSource(impl: CommentRoomsDataSourceImpl): CommentRoomsDataSource

    companion object {
        @Provides
        @Singleton
        @CommentRoomsApiServiceQualifier
        fun provideCommentRoomsService() : CommentApiService {
            return NetworkManager.commentService()
        }
    }
}
