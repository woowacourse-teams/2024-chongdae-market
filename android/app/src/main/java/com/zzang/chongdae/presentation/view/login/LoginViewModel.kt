package com.zzang.chongdae.presentation.view.login

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val context: Context,
) : ViewModel() {
    private val _navigateEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val navigateEvent: SingleLiveData<Boolean> get() = _navigateEvent

    fun postLogin(ci: String) {
        viewModelScope.launch {
            authRepository.saveLogin(
                ci = ci,
            ).onSuccess {
                Log.d("alsong", "login success")
                _navigateEvent.setValue(true)
            }.onFailure {
                Log.e("alsong", "postLogin ${it.message}")
                when (it.message) {
                    "404" -> postSignup(ci)
                    "401" -> postRefreshToken()
                }
            }
        }
    }

    private fun postSignup(ci: String) {
        viewModelScope.launch {
            authRepository.saveSignup(
                ci = ci,
            ).onSuccess {
                Log.d("alsong", "signup success")
                Log.d("alsong", "id: ${it.memberId}")
                Log.d("alsong", "nickName: ${it.nickName}")
                context.dataStore.edit { preferences ->
                    preferences[MEMBER_ID_KEY] = it.memberId
                    preferences[NICKNAME_KEY] = it.nickName
                }
                postSignup(ci)
            }.onFailure {
                Log.e("alsong", it.message.toString())
            }
        }
    }

    private fun postRefreshToken() {
        viewModelScope.launch {
            authRepository.saveRefresh().onSuccess {
            }.onFailure {
            }
        }
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "settings")
        val MEMBER_ID_KEY = longPreferencesKey("member_id_key")
        val NICKNAME_KEY = stringPreferencesKey("nickname_key")

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            authRepository: AuthRepository,
            context: Context,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return LoginViewModel(authRepository, context) as T
            }
        }
    }
}
