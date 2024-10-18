package com.zzang.chongdae.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
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

        fun postLogin(
            accessToken: String,
            fcmToken: String?,
        ) {
            viewModelScope.launch {
                when (val result = authRepository.saveLogin(accessToken = accessToken, fcmToken = fcmToken)) {
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
    }
