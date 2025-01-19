package com.zzang.chongdae.di.module

import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.source.ParticipantRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.ParticipantRepositoryImpl
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.di.annotations.ParticipantApiServiceQualifier
import com.zzang.chongdae.di.annotations.ParticipantDataSourceQualifier
import com.zzang.chongdae.di.annotations.ParticipantRepositoryQualifier
import com.zzang.chongdae.domain.repository.ParticipantRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ParticipantDependencyModule {
    @Binds
    @Singleton
    @ParticipantRepositoryQualifier
    abstract fun provideParticipantRepository(impl: ParticipantRepositoryImpl): ParticipantRepository

    @Binds
    @Singleton
    @ParticipantDataSourceQualifier
    abstract fun provideParticipantDataSource(impl: ParticipantRemoteDataSourceImpl): ParticipantRemoteDataSource

    companion object {
        @Provides
        @Singleton
        @ParticipantApiServiceQualifier
        fun provideParticipantApiService(apiService: ParticipationApiService): ParticipationApiService {
            return apiService
        }
    }
}
