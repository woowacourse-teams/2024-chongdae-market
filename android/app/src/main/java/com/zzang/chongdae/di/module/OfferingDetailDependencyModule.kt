package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.source.OfferingDetailDataSourceImpl
import com.zzang.chongdae.data.repository.OfferingDetailRepositoryImpl
import com.zzang.chongdae.data.source.OfferingDetailDataSource
import com.zzang.chongdae.di.annotations.OfferingDetailDataSourceQualifier
import com.zzang.chongdae.di.annotations.OfferingDetailRepositoryQualifier
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class OfferingDetailDependencyModule {
    @Binds
    @Singleton
    @OfferingDetailRepositoryQualifier
    abstract fun provideOfferingDetailRepository(impl: OfferingDetailRepositoryImpl): OfferingDetailRepository

    @Binds
    @Singleton
    @OfferingDetailDataSourceQualifier
    abstract fun provideOfferingDetailDataSource(impl: OfferingDetailDataSourceImpl): OfferingDetailDataSource
}
