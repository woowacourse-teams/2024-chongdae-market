package com.zzang.chongdae.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _ci: MutableLiveData<String> = MutableLiveData()
    val ci: LiveData<String> get() = _ci

    fun loadCi(input: String) {
        _ci.value = input
    }

    fun postSignup() {
        viewModelScope.launch {
            authRepository.saveSignup(
                ci = "알송의테스트2",
            ).onSuccess {
                Log.d("alsong", "success signup ")
                Log.d("alsong", "memberId: ${it.memberId}")
                Log.d("alsong", "nickName: ${it.nickName}")
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(authRepository: AuthRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return LoginViewModel(authRepository) as T
                }
            }
    }
}
