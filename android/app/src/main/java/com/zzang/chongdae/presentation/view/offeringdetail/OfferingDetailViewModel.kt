package com.zzang.chongdae.presentation.view.offeringdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.R
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.OfferingCondition.Companion.isAvailable
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.usecase.offeringdetail.DeleteOfferingUseCase
import com.zzang.chongdae.domain.usecase.offeringdetail.FetchOfferingDetailUseCase
import com.zzang.chongdae.domain.usecase.offeringdetail.SaveParticipationUseCase
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.common.OnAlertClickListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class OfferingDetailViewModel
    @AssistedInject
    constructor(
        @Assisted private val offeringId: Long,
        private val fetchOfferingDetailUseCase: FetchOfferingDetailUseCase,
        private val saveParticipationUseCase: SaveParticipationUseCase,
        private val deleteOfferingUseCase: DeleteOfferingUseCase,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        private val userPreferencesDataStore: UserPreferencesDataStore,
    ) : ViewModel(),
        OnParticipationClickListener,
        OnOfferingReportClickListener,
        OnMoveCommentDetailClickListener,
        OnProductLinkClickListener,
        OnOfferingModifyClickListener,
        OnAlertClickListener {
        @AssistedFactory
        interface OfferingDetailAssistedFactory {
            fun create(offeringId: Long): OfferingDetailViewModel
        }

        private val _offeringDetail: MutableLiveData<OfferingDetail> = MutableLiveData()
        val offeringDetail: LiveData<OfferingDetail> get() = _offeringDetail

        private val _currentCount: MutableLiveData<Int> = MutableLiveData()
        val currentCount: LiveData<Int> get() = _currentCount

        val purchaseCount: MutableLiveData<Int> = MutableLiveData(MIN_PURCHASE_COUNT)

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

        private val _error: MutableSingleLiveData<Int> = MutableSingleLiveData()
        val error: SingleLiveData<Int> get() = _error

        private val _modifyOfferingEvent: MutableSingleLiveData<Long> = MutableSingleLiveData()
        val modifyOfferingEvent: SingleLiveData<Long> get() = _modifyOfferingEvent

        private val _deleteOfferingEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val deleteOfferingEvent: SingleLiveData<Unit> get() = _deleteOfferingEvent

        private val _deleteOfferingSuccessEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val deleteOfferingSuccessEvent: SingleLiveData<Unit> get() = _deleteOfferingSuccessEvent

        private val _refreshTokenExpiredEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val refreshTokenExpiredEvent: SingleLiveData<Unit> get() = _refreshTokenExpiredEvent

        private val _showAlertEvent = MutableSingleLiveData<Unit>()
        val showAlertEvent: SingleLiveData<Unit> get() = _showAlertEvent

        private val _alertCancelEvent = MutableSingleLiveData<Unit>()
        val alertCancelEvent: SingleLiveData<Unit> get() = _alertCancelEvent

        private val _showBottomSheetDialogEvent = MutableSingleLiveData<Unit>()
        val showBottomSheetDialogEvent: SingleLiveData<Unit> get() = _showBottomSheetDialogEvent

        private val _isOfferingDetailLoading: MutableLiveData<Boolean> = MutableLiveData(false)
        val isOfferingDetailLoading: LiveData<Boolean> get() = _isOfferingDetailLoading

        private val _isParticipationLoading: MutableLiveData<Boolean> = MutableLiveData(false)
        val isParticipationLoading: LiveData<Boolean> get() = _isParticipationLoading

        init {
            loadOffering()
        }

        fun loadOffering() {
            viewModelScope.launch {
                _isOfferingDetailLoading.value = true
                when (val result = fetchOfferingDetailUseCase(offeringId)) {
                    is Result.Error ->
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> {
                                        loadOffering()
                                    }

                                    is Result.Error -> {
                                        _isOfferingDetailLoading.value = false
                                        userPreferencesDataStore.removeAllData()
                                        _refreshTokenExpiredEvent.setValue(Unit)
                                        return@launch
                                    }
                                }
                            }

                            DataError.Network.BAD_REQUEST -> {
                                _error.setValue(R.string.offering_detail_load_error_mesage)
                            }

                            else -> {
                                Log.e("error", "loadOffering Error: ${result.error.name}")
                            }
                        }

                    is Result.Success -> {
                        _isOfferingDetailLoading.value = false
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

        override fun participate() {
            viewModelScope.launch {
                _isParticipationLoading.value = true
                when (val result = saveParticipationUseCase(offeringId, purchaseCount.value ?: 1)) {
                    is Result.Error ->
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> participate()
                                    is Result.Error -> {
                                        userPreferencesDataStore.removeAllData()
                                        _refreshTokenExpiredEvent.setValue(Unit)
                                        return@launch
                                    }
                                }
                            }

                            DataError.Network.BAD_REQUEST -> {
                                _error.setValue(R.string.offering_detail_participation_error)
                                loadOffering()
                            }

                            else -> {
                                Log.e("error", "onClickParticipation Error: ${result.error.name}")
                            }
                        }

                    is Result.Success -> {
                        _isParticipationLoading.value = false
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

        override fun onClickOfferingModify() {
            if (_offeringCondition.value == OfferingCondition.CONFIRMED) {
                _error.setValue(R.string.error_modify_invalid)
                return
            }
            _modifyOfferingEvent.setValue(offeringId)
        }

        fun onClickDeleteButton() {
            _deleteOfferingEvent.setValue(Unit)
        }

        fun deleteOffering(offeringId: Long) {
            viewModelScope.launch {
                when (val result = deleteOfferingUseCase(offeringId)) {
                    is Result.Error ->
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> deleteOffering(offeringId)
                                    is Result.Error -> return@launch
                                }
                            }

                            DataError.Network.NULL -> {
                                _deleteOfferingSuccessEvent.setValue(Unit)
                            }

                            DataError.Network.BAD_REQUEST -> {
                                _error.setValue(R.string.offering_detail_delete_error)
                            }

                            else -> {
                                Log.e("error", "onClickOfferingDelete Error: ${result.error.name}")
                            }
                        }

                    is Result.Success -> {
                        _deleteOfferingSuccessEvent.setValue(Unit)
                    }
                }
            }
        }

        private fun isParticipationEnabled(
            offeringCondition: OfferingCondition,
            isParticipated: Boolean,
        ) = !isParticipated && offeringCondition.isAvailable()

        fun onParticipateClick() {
            _showAlertEvent.setValue(Unit)
        }

        override fun onClickConfirm() {
            _alertCancelEvent.setValue(Unit)
            participate()
        }

        override fun onClickCancel() {
            _alertCancelEvent.setValue(Unit)
        }

        fun showBottomSheetDialog() {
            _showBottomSheetDialogEvent.setValue(Unit)
        }

        fun increaseCount() {
            val totalCount = _offeringDetail.value?.totalCount ?: return
            val currentCount = _currentCount.value ?: return
            val purchaseCountValue = purchaseCount.value ?: return
            if (purchaseCountValue >= (totalCount - currentCount)) return
            purchaseCount.value = purchaseCountValue + 1
        }

        fun decreaseCount() {
            val purchaseCountValue = purchaseCount.value ?: return
            if (purchaseCountValue <= MIN_PURCHASE_COUNT) return
            purchaseCount.value = purchaseCountValue - 1
        }

        companion object {
            private const val DEFAULT_TITLE = ""
            private const val MIN_PURCHASE_COUNT = 1

            @Suppress("UNCHECKED_CAST")
            fun getFactory(
                assistedFactory: OfferingDetailAssistedFactory,
                offeringId: Long,
            ) = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(offeringId) as T
                }
            }
        }
    }
