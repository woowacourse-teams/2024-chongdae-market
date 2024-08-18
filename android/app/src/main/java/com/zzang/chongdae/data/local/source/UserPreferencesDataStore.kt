package com.zzang.chongdae.data.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(private val dataStore: DataStore<Preferences>) {
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

    companion object {
        val MEMBER_ID_KEY = longPreferencesKey("member_id_key")
        val NICKNAME_KEY = stringPreferencesKey("nickname_key")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
    }
}
