package com.zzang.chongdae.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.data.local.source.UserPreferencesDataStore
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {
    private val _loginSuccessEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
    val loginSuccessEvent: SingleLiveData<Unit> get() = _loginSuccessEvent

    private val _alreadyLoggedInEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
    val alreadyLoggedInEvent: SingleLiveData<Unit> get() = _alreadyLoggedInEvent

    init {
        makeAlreadyLoggedInEvent()
    }

    private fun makeAlreadyLoggedInEvent() {
        viewModelScope.launch {
            val accessToken = userPreferencesDataStore.accessTokenFlow.first()
            if (accessToken != null) {
                _alreadyLoggedInEvent.setValue(Unit)
            }
        }
    }

    fun postLogin(accessToken: String) {
        viewModelScope.launch {
            when (val result = authRepository.saveLogin(accessToken = accessToken)) {
                is Result.Success -> {
                    userPreferencesDataStore.saveMember(result.data.memberId, result.data.nickName)
                    _loginSuccessEvent.setValue(Unit)
                }

                is Result.Error -> {
                    Log.e("error", "postLogin: ${result.error}")
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            authRepository: AuthRepository,
            userPreferencesDataStore: UserPreferencesDataStore,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return LoginViewModel(authRepository, userPreferencesDataStore) as T
            }
        }
    }
}
