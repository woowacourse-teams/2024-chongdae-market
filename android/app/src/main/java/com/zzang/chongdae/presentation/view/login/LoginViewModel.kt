package com.zzang.chongdae.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.CheckAlreadyLoggedInUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostLoginUseCaseQualifier
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.login.usecase.CheckIfAlreadyLoggedInUseCase
import com.zzang.chongdae.presentation.view.login.usecase.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        @CheckAlreadyLoggedInUseCaseQualifier private val checkIfAlreadyLoggedInUseCase: CheckIfAlreadyLoggedInUseCase,
        @PostLoginUseCaseQualifier private val postLoginUseCase: PostLoginUseCase,
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
                val isAlreadyLoggedIn = checkIfAlreadyLoggedInUseCase()
                if (isAlreadyLoggedIn) {
                    _alreadyLoggedInEvent.setValue(Unit)
                }
            }
        }

        fun postLogin(
            accessToken: String,
            fcmToken: String,
        ) {
            viewModelScope.launch {
                when (val result = postLoginUseCase(accessToken = accessToken, fcmToken = fcmToken)) {
                    is Result.Success -> {
                        _loginSuccessEvent.setValue(Unit)
                    }

                    is Result.Error -> {
                        Log.e("error", "postLogin: ${result.error}")
                    }
                }
            }
        }
    }
