package com.zzang.chongdae.presentation.view.offeringdetail

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.OfferingCondition.Companion.isAvailable
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OfferingDetailViewModel(
    private val offeringId: Long,
    private val offeringDetailRepository: OfferingDetailRepository,
    private val context: Context,
) : ViewModel(), OnParticipationClickListener {
    private val _offeringDetail: MutableLiveData<OfferingDetail> = MutableLiveData()
    val offeringDetail: LiveData<OfferingDetail> get() = _offeringDetail

    private val _currentCount: MutableLiveData<Int> = MutableLiveData()
    val currentCount: LiveData<Int> get() = _currentCount

    private val _offeringCondition: MutableLiveData<OfferingCondition> = MutableLiveData()
    val offeringCondition: LiveData<OfferingCondition> get() = _offeringCondition

    private val _isParticipated: MutableLiveData<Boolean> = MutableLiveData(true)
    val isParticipated: LiveData<Boolean> get() = _isParticipated

    private val _isAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isAvailable: LiveData<Boolean> get() = _isAvailable

    private val _isRepresentative: MutableLiveData<Boolean> = MutableLiveData(true)
    val isRepresentative: LiveData<Boolean> get() = _isRepresentative

    private val _commentDetailEvent: MutableSingleLiveData<String> = MutableSingleLiveData()
    val commentDetailEvent: SingleLiveData<String> get() = _commentDetailEvent

    init {
        loadOffering()
    }

    private fun loadOffering() {
        viewModelScope.launch {
            val pref = context.dataStore.data.first()

            offeringDetailRepository.fetchOfferingDetail(
                memberId = BuildConfig.TOKEN.toLong(),
                offeringId = offeringId,
            )
                .onSuccess {
                    _offeringDetail.value = it
                    _currentCount.value = it.currentCount.value
                    _offeringCondition.value = it.condition
                    _isParticipated.value = it.isParticipated
                    _isAvailable.value = isParticipationEnabled(it.condition, it.isParticipated)
                    _isRepresentative.value = isRepresentative(it, pref[MEMBER_ID_KEY] ?: -1L)
                }.onFailure {
                    Log.e("error", it.message.toString())
                }
        }
    }

    override fun onClickParticipation() {
        viewModelScope.launch {
            offeringDetailRepository.saveParticipation(
                memberId = BuildConfig.TOKEN.toLong(),
                offeringId = offeringId,
            ).onSuccess {
                _isParticipated.value = true
                _isAvailable.value = false
                _commentDetailEvent.setValue(offeringDetail.value?.title ?: DEFAULT_TITLE)
            }.onFailure {
                Log.e("Error", it.message.toString())
            }
        }
    }

    private fun isParticipationEnabled(
        offeringCondition: OfferingCondition,
        isParticipated: Boolean,
    ) = offeringCondition.isAvailable() && !isParticipated

    // 총대여부를 확인하는 메서드(로그인 기능 구현 시 수정 예정)
    private fun isRepresentative(it: OfferingDetail, memberId: Long): Boolean {
        return it.memberId == memberId.toString()
    }

    companion object {
        private const val DEFAULT_TITLE = ""
        val Context.dataStore by preferencesDataStore(name = "settings")
        val MEMBER_ID_KEY = longPreferencesKey("member_id_key")

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            offeringDetailRepository: OfferingDetailRepository,
            context: Context
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingDetailViewModel(
                    offeringId,
                    offeringDetailRepository,
                    context
                ) as T
            }
        }
    }
}
