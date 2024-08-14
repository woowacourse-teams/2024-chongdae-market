package com.zzang.chongdae.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.data.local.source.MemberDataStore

class MyPageViewModel(memberDataStore: MemberDataStore) : ViewModel() {
    val nickName: LiveData<String?> = memberDataStore.nickNameFlow.asLiveData()
    
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
