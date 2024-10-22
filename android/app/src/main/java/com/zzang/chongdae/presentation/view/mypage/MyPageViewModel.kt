package com.zzang.chongdae.presentation.view.mypage

import android.app.NotificationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.common.OnAlertClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
@Inject
constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore,
) : ViewModel(),
    OnAlertClickListener {
    val nickName: LiveData<String?> = userPreferencesDataStore.nickNameFlow.asLiveData()

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

    val isNotificationActivate = MutableLiveData<Boolean>(true)
    val isNotificationImportanceHigh = MutableLiveData<Boolean>(true)

    init {
        initNotificationSwitches()
    }

    private fun initNotificationSwitches() {
        viewModelScope.launch {
            val notificationActivate = userPreferencesDataStore.notificationActivateFlow.first()
            when (notificationActivate) {
                true -> isNotificationActivate.value = true
                false -> isNotificationActivate.value = false
            }

            val notificationImportance = userPreferencesDataStore.notificationImportanceFlow.first()
            when (notificationImportance) {
                NotificationManager.IMPORTANCE_HIGH -> isNotificationImportanceHigh.value = true

                NotificationManager.IMPORTANCE_DEFAULT -> isNotificationImportanceHigh.value = false
            }
        }
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
            userPreferencesDataStore.removeAllData()
        }
        _logoutEvent.setValue(Unit)
    }

    override fun onClickCancel() {
        _alertCancelEvent.setValue(Unit)
    }

    fun onNotificationActivateSwitchChanged(isChecked: Boolean) {
        isNotificationActivate.value = isChecked
        viewModelScope.launch {
            when (isChecked) {
                true -> userPreferencesDataStore.setNotificationActivate(true)
                false -> userPreferencesDataStore.setNotificationActivate(false)
            }
        }
    }

    fun onNotificationImportanceSwitchChanged(isChecked: Boolean) {
        isNotificationImportanceHigh.value = isChecked
        viewModelScope.launch {
            when (isChecked) {
                true -> userPreferencesDataStore.setNotificationImportance(NotificationManager.IMPORTANCE_HIGH)
                false -> userPreferencesDataStore.setNotificationImportance(NotificationManager.IMPORTANCE_DEFAULT)
            }
        }
    }
}
