package com.zzang.chongdae.di.feature.offering

import com.zzang.chongdae.data.local.source.OfferingLocalDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.OfferingRepositoryImpl
import com.zzang.chongdae.data.source.offering.OfferingLocalDataSource
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.domain.repository.OfferingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OfferingDependencyModule {
    @Binds
    @Singleton
    abstract fun provideOfferingRepository(impl: OfferingRepositoryImpl): OfferingRepository

    @Binds
    @Singleton
    abstract fun provideOfferingRemoteDataSource(impl: OfferingRemoteDataSourceImpl): OfferingRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideOfferingLocalDataSource(impl: OfferingLocalDataSourceImpl): OfferingLocalDataSource
}
