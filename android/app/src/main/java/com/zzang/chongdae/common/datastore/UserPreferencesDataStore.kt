package com.zzang.chongdae.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.zzang.chongdae.di.annotations.DataStoreQualifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore
@Inject
constructor(
    @DataStoreQualifier private val dataStore: DataStore<Preferences>,
) {
    val memberIdFlow: Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[MEMBER_ID_KEY]
        }

    val nickNameFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[NICKNAME_KEY]
        }

    val accessTokenFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    val refreshTokenFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }

    val notificationImportanceFlow: Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[NOTIFICATION_IMPORTANCE_KEY] ?: DEFAULT_NOTIFICATION_IMPORTANCE
        }

    suspend fun saveMember(
        memberId: Long,
        nickName: String,
    ) {
        dataStore.edit { preferences ->
            preferences[MEMBER_ID_KEY] = memberId
            preferences[NICKNAME_KEY] = nickName
        }
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun removeAllData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val MEMBER_ID_KEY = longPreferencesKey("member_id_key")
        private val NICKNAME_KEY = stringPreferencesKey("nickname_key")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
        private val NOTIFICATION_IMPORTANCE_KEY = intPreferencesKey("notification_importance_key")
        private const val DEFAULT_NOTIFICATION_IMPORTANCE = 3
    }
}
