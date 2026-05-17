package com.zzang.chongdae.di.core

import com.zzang.chongdae.data.remote.source.AnalyticsDataSourceImpl
import com.zzang.chongdae.data.repository.AnalyticsRepositoryImpl
import com.zzang.chongdae.data.source.AnalyticsDataSource
import com.zzang.chongdae.domain.repository.AnalyticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AnalyticsDependencyModule {
    @Binds
    @Singleton
    abstract fun provideAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

    @Binds
    @Singleton
    abstract fun provideAnalyticsDataSource(impl: AnalyticsDataSourceImpl): AnalyticsDataSource
}
