package com.zzang.chongdae.presentation.view.mypage

import android.app.NotificationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.domain.usecase.mypage.GetNickNameUseCase
import com.zzang.chongdae.domain.usecase.mypage.GetNotificationActivateUseCase
import com.zzang.chongdae.domain.usecase.mypage.GetNotificationImportanceUseCase
import com.zzang.chongdae.domain.usecase.mypage.LogoutUseCase
import com.zzang.chongdae.domain.usecase.mypage.SetNotificationActivateUseCase
import com.zzang.chongdae.domain.usecase.mypage.SetNotificationImportanceUseCase
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.common.OnAlertClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        getNickNameUseCase: GetNickNameUseCase,
        private val getNotificationActivateUseCase: GetNotificationActivateUseCase,
        private val getNotificationImportanceUseCase: GetNotificationImportanceUseCase,
        private val setNotificationActivateUseCase: SetNotificationActivateUseCase,
        private val setNotificationImportanceUseCase: SetNotificationImportanceUseCase,
        private val logoutUseCase: LogoutUseCase,
    ) : ViewModel(), OnAlertClickListener {
        val nickName: LiveData<String?> = getNickNameUseCase().asLiveData()

        private val _openUrlInBrowserEvent = MutableSingleLiveData<String>()
        val openUrlInBrowserEvent: SingleLiveData<String> get() = _openUrlInBrowserEvent

        private val _logoutEvent = MutableSingleLiveData<Unit>()
        val logoutEvent: SingleLiveData<Unit> get() = _logoutEvent

        private val _showAlertEvent = MutableSingleLiveData<Unit>()
        val showAlertEvent: SingleLiveData<Unit> get() = _showAlertEvent

        private val _alertCancelEvent = MutableSingleLiveData<Unit>()
        val alertCancelEvent: SingleLiveData<Unit> get() = _alertCancelEvent

        private val termsOfUseUrl =
            "https://silent-apparatus-578.notion.site/f1f5cd1609d4469dba3ab7d0f95c183c?pvs=4"
        private val privacyUrl =
            "https://silent-apparatus-578.notion.site/f1f5cd1609d4469dba3ab7d0f95c183c?pvs=4"
        private val withdrawalUrl = "https://forms.gle/z5MUzVTUoyunfqEu8"

        val isNotificationActivate = MutableLiveData(true)
        val isNotificationImportanceHigh = MutableLiveData(true)

        init {
            viewModelScope.launch {
                initNotificationSwitches()
            }
        }

    private suspend fun initNotificationSwitches() {
        isNotificationActivate.value = getNotificationActivateUseCase()
        isNotificationImportanceHigh.value =
            getNotificationImportanceUseCase() == NotificationManager.IMPORTANCE_HIGH
    }

        fun onClickTermsOfUse() {
            _openUrlInBrowserEvent.setValue(termsOfUseUrl)
        }

        fun onClickPrivacy() {
            _openUrlInBrowserEvent.setValue(privacyUrl)
        }

        fun onClickLogout() {
            _showAlertEvent.setValue(Unit)
        }

        fun onClickWithdrawal() {
            _openUrlInBrowserEvent.setValue(withdrawalUrl)
        }

        override fun onClickConfirm() {
            viewModelScope.launch {
                logoutUseCase()
                _logoutEvent.setValue(Unit)
            }
        }

        override fun onClickCancel() {
            _alertCancelEvent.setValue(Unit)
        }

        fun onNotificationActivateSwitchChanged(isChecked: Boolean) {
            isNotificationActivate.value = isChecked
            viewModelScope.launch {
                setNotificationActivateUseCase(isChecked)
            }
        }

        fun onNotificationImportanceSwitchChanged(isChecked: Boolean) {
            isNotificationImportanceHigh.value = isChecked
            viewModelScope.launch {
                val importance =
                    if (isChecked) {
                        NotificationManager.IMPORTANCE_HIGH
                    } else {
                        NotificationManager.IMPORTANCE_DEFAULT
                    }
                setNotificationImportanceUseCase(importance)
            }
        }
    }
