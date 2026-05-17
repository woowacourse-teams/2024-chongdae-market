package com.zzang.chongdae.di.feature.commentroom

import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.source.CommentRoomsDataSource
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CommentRoomsDependencyModule {
    @Binds
    @Singleton
    abstract fun provideCommentRoomsRepository(impl: CommentRoomsRepositoryImpl): CommentRoomsRepository

    @Binds
    @Singleton
    abstract fun provideCommentRoomsDataSource(impl: CommentRoomsDataSourceImpl): CommentRoomsDataSource
}
