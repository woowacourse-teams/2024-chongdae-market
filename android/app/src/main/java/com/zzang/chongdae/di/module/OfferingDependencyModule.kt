package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.api.OfferingApiService
import com.zzang.chongdae.data.remote.source.OfferingRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.OfferingRepositoryImpl
import com.zzang.chongdae.data.source.offering.OfferingRemoteDataSource
import com.zzang.chongdae.di.annotations.OfferingApiServiceQualifier
import com.zzang.chongdae.di.annotations.OfferingDataSourceQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.domain.repository.OfferingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class OfferingDependencyModule {
    @Binds
    @Singleton
    @OfferingRepositoryQualifier
    abstract fun provideOfferingRepository(impl: OfferingRepositoryImpl): OfferingRepository

    @Binds
    @Singleton
    @OfferingDataSourceQualifier
    abstract fun provideOfferingDataSource(impl: OfferingRemoteDataSourceImpl): OfferingRemoteDataSource

    companion object {
        @Provides
        @Singleton
        @OfferingApiServiceQualifier
        fun provideOfferingService(): OfferingApiService {
            return NetworkManager.offeringService()
        }
    }
}
