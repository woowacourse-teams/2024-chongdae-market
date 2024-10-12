package com.zzang.chongdae.di.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.ChongdaeApp.Companion.dataStore
import com.zzang.chongdae.di.annotations.DataStoreQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStoreDependencyModule {
    @Provides
    @Singleton
    @DataStoreQualifier
    fun provideDataStore(): DataStore<Preferences> {
        return ChongdaeApp.chongdaeAppContext.dataStore
    }
}
