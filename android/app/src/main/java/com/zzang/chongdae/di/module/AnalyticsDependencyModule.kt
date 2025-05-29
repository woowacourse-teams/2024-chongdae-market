package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.source.AnalyticsDataSourceImpl
import com.zzang.chongdae.data.repository.AnalyticsRepositoryImpl
import com.zzang.chongdae.data.source.AnalyticsDataSource
import com.zzang.chongdae.di.annotations.AnalyticsDataSourceQualifier
import com.zzang.chongdae.di.annotations.AnalyticsRepositoryQualifier
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
    @AnalyticsRepositoryQualifier
    abstract fun provideAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

    @Binds
    @Singleton
    @AnalyticsDataSourceQualifier
    abstract fun provideAnalyticsDataSource(impl: AnalyticsDataSourceImpl): AnalyticsDataSource
}
