package com.zzang.chongdae.presentation.view.offeringdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.R
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
) : ViewModel(), OnParticipationClickListener, OnOfferingReportClickListener {
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

    private val _updatedOfferingId: MutableLiveData<Long> = MutableLiveData()
    val updatedOfferingId: LiveData<Long> get() = _updatedOfferingId

    private val _reportEvent: MutableSingleLiveData<Int> = MutableSingleLiveData()
    val reportEvent: SingleLiveData<Int> get() = _reportEvent

    init {
        loadOffering()
    }

    private fun loadOffering() {
        viewModelScope.launch {
            offeringDetailRepository.fetchOfferingDetail(
                offeringId = offeringId,
            )
                .onSuccess {
                    _offeringDetail.value = it
                    _currentCount.value = it.currentCount.value
                    _offeringCondition.value = it.condition
                    _isParticipated.value = it.isParticipated
                    _isAvailable.value = isParticipationEnabled(it.condition, it.isParticipated)
                    _isRepresentative.value = it.isProposer
                }.onFailure {
                    Log.e("error", it.message.toString())
                }
        }
    }

    override fun onClickParticipation() {
        viewModelScope.launch {
            offeringDetailRepository.saveParticipation(
                offeringId = offeringId,
            ).onSuccess {
                _isParticipated.value = true
                _isAvailable.value = false
                _commentDetailEvent.setValue(offeringDetail.value?.title ?: DEFAULT_TITLE)
                _updatedOfferingId.value = offeringId
            }.onFailure {
                Log.e("Error", it.message.toString())
            }
        }
    }

    override fun onClickReport() {
        _reportEvent.setValue(R.string.report_url)
    }

    private fun isParticipationEnabled(
        offeringCondition: OfferingCondition,
        isParticipated: Boolean,
    ) = offeringCondition.isAvailable() && !isParticipated

    companion object {
        private const val DEFAULT_TITLE = ""

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            offeringDetailRepository: OfferingDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingDetailViewModel(
                    offeringId,
                    offeringDetailRepository,
                ) as T
            }
        }
    }
}
