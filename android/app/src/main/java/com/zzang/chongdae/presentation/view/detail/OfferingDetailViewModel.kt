package com.zzang.chongdae.presentation.view.detail

import android.util.Log
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
import kotlinx.coroutines.launch

class OfferingDetailViewModel(
    private val offeringId: Long,
    private val offeringDetailRepository: OfferingDetailRepository,
) : ViewModel(), ParticipationClickListener {
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
        loadArticle()
    }

    private fun loadArticle() {
        viewModelScope.launch {
            // memberId를 가져 오는 로직 수정 예정(로그인 기능 구현 시)
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
                    _isRepresentative.value = isRepresentative(it)
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
    private fun isRepresentative(it: OfferingDetail) = it.memberId == BuildConfig.TOKEN

    companion object {
        private const val DEFAULT_TITLE = ""

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            articleId: Long,
            offeringDetailRepository: OfferingDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingDetailViewModel(
                    articleId,
                    offeringDetailRepository,
                ) as T
            }
        }
    }
}
