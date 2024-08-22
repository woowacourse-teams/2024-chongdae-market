package com.zzang.chongdae.presentation.view.offeringdetail

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
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class OfferingDetailViewModel(
    private val offeringId: Long,
    private val offeringDetailRepository: OfferingDetailRepository,
    private val authRepository: AuthRepository,
) : ViewModel(),
    OnParticipationClickListener,
    OnOfferingReportClickListener,
    OnMoveCommentDetailClickListener,
    OnProductLinkClickListener {
    private val _offeringDetail: MutableLiveData<OfferingDetail> = MutableLiveData()
    val offeringDetail: LiveData<OfferingDetail> get() = _offeringDetail

    private val _currentCount: MutableLiveData<Int> = MutableLiveData()
    val currentCount: LiveData<Int> get() = _currentCount

    private val _offeringCondition: MutableLiveData<OfferingCondition> = MutableLiveData()
    val offeringCondition: LiveData<OfferingCondition> get() = _offeringCondition

    private val _isParticipated: MutableLiveData<Boolean> = MutableLiveData(false)
    val isParticipated: LiveData<Boolean> get() = _isParticipated

    private val _isParticipationAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val isParticipationAvailable: LiveData<Boolean> get() = _isParticipationAvailable

    private val _isRepresentative: MutableLiveData<Boolean> = MutableLiveData(true)
    val isRepresentative: LiveData<Boolean> get() = _isRepresentative

    private val _commentDetailEvent: MutableSingleLiveData<String> = MutableSingleLiveData()
    val commentDetailEvent: SingleLiveData<String> get() = _commentDetailEvent

    private val _updatedOfferingId: MutableLiveData<Long> = MutableLiveData()
    val updatedOfferingId: LiveData<Long> get() = _updatedOfferingId

    private val _reportEvent: MutableSingleLiveData<Int> = MutableSingleLiveData()
    val reportEvent: SingleLiveData<Int> get() = _reportEvent

    private val _productLinkRedirectEvent: MutableSingleLiveData<String> = MutableSingleLiveData()
    val productLinkRedirectEvent: SingleLiveData<String> get() = _productLinkRedirectEvent

    init {
        loadOffering()
    }

    private fun loadOffering() {
        viewModelScope.launch {
            when (val result = offeringDetailRepository.fetchOfferingDetail(offeringId)) {
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            loadOffering()
                        }

                        else -> DataError.Network.UNKNOWN
                    }

                is Result.Success -> {
                    _offeringDetail.value = result.data
                    _currentCount.value = result.data.currentCount.value
                    _offeringCondition.value = result.data.condition
                    _isParticipated.value = result.data.isParticipated
                    _isParticipationAvailable.value =
                        isParticipationEnabled(result.data.condition, result.data.isParticipated)
                    _isRepresentative.value = result.data.isProposer
                }
            }
        }
    }

    override fun onClickParticipation() {
        viewModelScope.launch {
            when (val result = offeringDetailRepository.saveParticipation(offeringId)) {
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            onClickParticipation()
                        }

                        else -> DataError.Network.UNKNOWN
                    }

                is Result.Success -> {
                    _isParticipated.value = true
                    _commentDetailEvent.setValue(offeringDetail.value?.title ?: DEFAULT_TITLE)
                    _updatedOfferingId.value = offeringId
                }
            }
        }
    }

    override fun onClickMoveCommentDetail() {
        _commentDetailEvent.setValue(offeringDetail.value?.title ?: DEFAULT_TITLE)
    }

    override fun onClickReport() {
        _reportEvent.setValue(R.string.report_url)
    }

    override fun onClickProductRedirectText(productUrl: String) {
        _productLinkRedirectEvent.setValue(productUrl)
    }

    private fun isParticipationEnabled(
        offeringCondition: OfferingCondition,
        isParticipated: Boolean,
    ) = !isParticipated && offeringCondition.isAvailable()

    companion object {
        private const val DEFAULT_TITLE = ""

        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            offeringDetailRepository: OfferingDetailRepository,
            authRepository: AuthRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return OfferingDetailViewModel(
                    offeringId,
                    offeringDetailRepository,
                    authRepository,
                ) as T
            }
        }
    }
}
