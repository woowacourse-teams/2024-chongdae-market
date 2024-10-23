package com.zzang.chongdae.presentation.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : ViewModel() {
        private val _fcmTokenEmptyEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val fcmTokenEmptyEvent: SingleLiveData<Unit> get() = _fcmTokenEmptyEvent

        init {
            makeFcmTokenEmptyEvent()
        }

        private fun makeFcmTokenEmptyEvent() {
            viewModelScope.launch {
                if (userPreferencesDataStore.fcmTokenFlow.first() != null) return@launch
                userPreferencesDataStore.removeAllData()
                _fcmTokenEmptyEvent.setValue(Unit)
            }
        }
    }
