package com.zzang.chongdae.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.auth.domain.usecase.IsUserLoggedInUseCase
import com.zzang.chongdae.auth.domain.usecase.PostLoginUseCase
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
        private val postLoginUseCase: PostLoginUseCase,
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
                val isAlreadyLoggedIn = isUserLoggedInUseCase()
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
