package com.zzang.chongdae.data.local.repository

import com.zzang.chongdae.data.local.datastore.UserPreferencesDataStore
import com.zzang.chongdae.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {
    override val accessTokenFlow: Flow<String?> = dataStore.accessTokenFlow
    override val refreshTokenFlow: Flow<String?> = dataStore.refreshTokenFlow
    override val notificationActivateFlow: Flow<Boolean> = dataStore.notificationActivateFlow
    override val notificationImportanceFlow: Flow<Int> = dataStore.notificationImportanceFlow
    override val nickNameFlow: Flow<String?> = dataStore.nickNameFlow

    override suspend fun saveMember(memberId: Long, nickname: String) {
        dataStore.saveMember(memberId, nickname)
    }

    override suspend fun saveFcmToken(fcmToken: String) {
        dataStore.saveFcmToken(fcmToken)
    }

    override suspend fun saveNickname(nickname: String) {
        dataStore.saveNickname(nickname)
    }

    override suspend fun setNotificationActivate(isActivate: Boolean) {
        dataStore.setNotificationActivate(isActivate)
    }

    override suspend fun setNotificationImportance(importance: Int) {
        dataStore.setNotificationImportance(importance)
    }

    override suspend fun removeAllData() {
        dataStore.removeAllData()
    }

    override suspend fun saveAccountTokens(accessToken: String, refreshToken: String) {
        dataStore.saveAccountTokens(accessToken, refreshToken)
    }
}
