package com.zzang.chongdae.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.data.local.source.MemberDataStore
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData

class MyPageViewModel(private val memberDataStore: MemberDataStore) : ViewModel() {
    val nickName: LiveData<String?> = memberDataStore.nickNameFlow.asLiveData()

    private val _openUrlInBrowserEvent = MutableSingleLiveData<String>()
    val openUrlInBrowserEvent: SingleLiveData<String> get() = _openUrlInBrowserEvent

    private val _logoutEvent = MutableSingleLiveData<Unit>()
    val logoutEvent: SingleLiveData<Unit> get() = _logoutEvent

    private val termsOfUseUrl =
        "https://silent-apparatus-578.notion.site/f1f5cd1609d4469dba3ab7d0f95c183c?pvs=4"
    private val privacyUrl =
        "https://silent-apparatus-578.notion.site/f1f5cd1609d4469dba3ab7d0f95c183c?pvs=4"
    private val withdrawalUrl = "https://forms.gle/z5MUzVTUoyunfqEu8"

    fun onClickTermsOfUse() {
        _openUrlInBrowserEvent.setValue(termsOfUseUrl)
    }

    fun onClickPrivacy() {
        _openUrlInBrowserEvent.setValue(privacyUrl)
    }

    fun onClickLogout() {
        _logoutEvent.setValue(Unit)
    }

    fun onClickWithdrawal() {
        _openUrlInBrowserEvent.setValue(withdrawalUrl)
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(memberDataStore: MemberDataStore) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return MyPageViewModel(memberDataStore) as T
                }
            }
    }
}