package com.zzang.chongdae.di.core

import android.content.Context
import androidx.room.Room
import com.zzang.chongdae.data.local.dao.CommentDao
import com.zzang.chongdae.data.local.dao.OfferingDao
import com.zzang.chongdae.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "chongdae_database",
        ).build()

    @Provides
    @Singleton
    fun provideOfferingDao(database: AppDatabase): OfferingDao = database.offeringDao()

    @Provides
    @Singleton
    fun provideCommentDao(database: AppDatabase): CommentDao = database.commentDao()
}
