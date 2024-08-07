package com.zzang.chongdae.presentation.view.login

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {
    fun postSignup(ci: String) {
        viewModelScope.launch {
            authRepository.saveSignup(
                ci = ci,
            ).onSuccess {
                Log.d("alsong", "signup success")
                context.dataStore.edit { preferences ->
                    preferences[MEMBER_ID_KEY] = it.memberId
                    preferences[NICKNAME_KEY] = it.nickName
                }
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "settings")
        val MEMBER_ID_KEY = longPreferencesKey("member_id_key")
        val NICKNAME_KEY = stringPreferencesKey("nickname_key")

        @Suppress("UNCHECKED_CAST")
        fun getFactory(authRepository: AuthRepository, context: Context) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return LoginViewModel(authRepository, context) as T
                }
            }
    }
}
