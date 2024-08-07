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
import org.apache.commons.lang3.mutable.Mutable

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _nickName: MutableLiveData<String> = MutableLiveData()
    val nickName: LiveData<String> get() = _nickName

    private val _memberId: MutableLiveData<Long> = MutableLiveData()
    val memberId: LiveData<Long> get() = _memberId

    fun postSignup(ci: String) {
        viewModelScope.launch {
            authRepository.saveSignup(
                ci = ci,
            ).onSuccess {
                Log.d("alsong", "signup success")
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
