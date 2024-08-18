package com.zzang.chongdae.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.data.local.source.UserPreferencesDataStore
import com.zzang.chongdae.domain.model.HttpStatusCode
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {
    private val _navigateEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
    val navigateEvent: SingleLiveData<Unit> get() = _navigateEvent

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

    fun postLogin(ci: String) {
        viewModelScope.launch {
            authRepository.saveLogin(
                ci = ci,
            ).onSuccess {
                userPreferencesDataStore.saveMember(it.memberId, it.nickName)
                _navigateEvent.setValue(Unit)
            }.onFailure {
                Log.e("error", "postLogin: ${it.message}")
                when (it.message) {
                    HttpStatusCode.NOT_FOUND_404.code -> postSignup(ci)
                    HttpStatusCode.UNAUTHORIZED_401.code -> postRefreshToken(ci)
                }
            }
        }
    }

    private fun postSignup(ci: String) {
        viewModelScope.launch {
            authRepository.saveSignup(
                ci = ci,
            ).onSuccess {
                userPreferencesDataStore.saveMember(it.memberId, it.nickName)
                postLogin(ci)
            }.onFailure {
                Log.e("error", "postSignup: ${it.message}")
                when (it.message) {
                    HttpStatusCode.NOT_FOUND_404.code -> postLogin(ci)
                    HttpStatusCode.UNAUTHORIZED_401.code -> postRefreshToken(ci)
                }
            }
        }
    }

    private fun postRefreshToken(ci: String) {
        viewModelScope.launch {
            authRepository.saveRefresh().onSuccess {
            }.onFailure {
                Log.e("error", "postRefresh: ${it.message}")
                when (it.message) {
                    HttpStatusCode.UNAUTHORIZED_401.code -> postLogin(ci)
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
