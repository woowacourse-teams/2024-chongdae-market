package com.zzang.chongdae.data.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemberPreferences(private val dataStore: DataStore<Preferences>) {
    val memberIdFlow: Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[MEMBER_ID_KEY]
        }

    val nickNameFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[NICKNAME_KEY]
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

    companion object {
        val MEMBER_ID_KEY = longPreferencesKey("member_id_key")
        val NICKNAME_KEY = stringPreferencesKey("nickname_key")
    }
}
