package com.zzang.chongdae.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDataStore : DataStore<Preferences> {
    private val _data = MutableStateFlow<Preferences>(emptyPreferences())
    override val data: Flow<Preferences>
        get() = _data

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val currentPreferences = _data.value
        val newPreferences = transform(currentPreferences)
        _data.value = newPreferences
        return newPreferences
    }
}
