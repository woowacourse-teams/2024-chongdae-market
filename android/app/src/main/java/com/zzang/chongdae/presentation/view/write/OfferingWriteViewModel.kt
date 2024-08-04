package com.zzang.chongdae.presentation.view.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.repository.OfferingWriteRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class OfferingWriteViewModel(
    private val offeringWriteRepository: OfferingWriteRepository,
) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData("")

    val productUrl: MutableLiveData<String?> = MutableLiveData("")

    val thumbnailUrl: MutableLiveData<String?> = MutableLiveData("")

    val totalCount: MutableLiveData<String> = MutableLiveData("2")

    val totalPrice: MutableLiveData<String> = MutableLiveData("")

    val eachPrice: MutableLiveData<String?> = MutableLiveData("")

    val meetingAddress: MutableLiveData<String> = MutableLiveData("")

    private val _meetingAddressDong: MutableLiveData<String?> = MutableLiveData()
    val meetingAddressDong: LiveData<String?> get() = _meetingAddressDong

    val meetingAddressDetail: MutableLiveData<String> = MutableLiveData("")

    val deadline: MutableLiveData<String> = MutableLiveData("")

    val description: MutableLiveData<String> = MutableLiveData("")

    private val _submitButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
    val submitButtonEnabled: LiveData<Boolean> get() = _submitButtonEnabled

    private val _invalidTotalCountEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val invalidTotalCountEvent: SingleLiveData<Boolean> get() = _invalidTotalCountEvent

    private val _invalidTotalPriceEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val invalidTotalPriceEvent: SingleLiveData<Boolean> get() = _invalidTotalPriceEvent

    private val _invalidEachPriceEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val invalidEachPriceEvent: SingleLiveData<Boolean> get() = _invalidEachPriceEvent

    private val _splitPrice: MediatorLiveData<Int> = MediatorLiveData(ERROR_INTEGER_FORMAT)
    val splitPrice: LiveData<Int> get() = _splitPrice

    private val _discountRate: MediatorLiveData<Float> = MediatorLiveData(ERROR_FLOAT_FORMAT)
    val discountRate: LiveData<Float> get() = _discountRate

    init {
        _submitButtonEnabled.apply {
            addSource(title) { updateSubmitButtonEnabled() }
            addSource(totalCount) { updateSubmitButtonEnabled() }
            addSource(totalPrice) { updateSubmitButtonEnabled() }
            addSource(meetingAddress) { updateSubmitButtonEnabled() }
            addSource(meetingAddressDetail) { updateSubmitButtonEnabled() }
            addSource(deadline) { updateSubmitButtonEnabled() }
            addSource(description) { updateSubmitButtonEnabled() }
        }

        _splitPrice.apply {
            addSource(totalCount) { updateSplitPrice() }
            addSource(totalPrice) { updateSplitPrice() }
        }

        _discountRate.apply {
            addSource(_splitPrice) { updateDiscountRate() }
            addSource(eachPrice) { updateDiscountRate() }
        }
    }

    private fun updateSubmitButtonEnabled() {
        _submitButtonEnabled.value = !title.value.isNullOrBlank() &&
            !totalCount.value.isNullOrBlank() &&
            !totalPrice.value.isNullOrBlank() &&
            !meetingAddress.value.isNullOrBlank() &&
            !meetingAddressDetail.value.isNullOrBlank() &&
            !deadline.value.isNullOrBlank() &&
            !description.value.isNullOrBlank()
    }

    private fun updateSplitPrice() {
        val totalPriceInt = totalPrice.value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalPriceInt < 0) {
            _splitPrice.value = ERROR_INTEGER_FORMAT
            return
        }
        val totalCountInt = totalCount.value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalCountInt <= 0) {
            _splitPrice.value = ERROR_INTEGER_FORMAT
            return
        }
        _splitPrice.value = totalPriceInt / totalCountInt
    }

    private fun updateDiscountRate() {
        val eachPriceInt = eachPrice.value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (eachPriceInt <= 0) {
            _discountRate.value = ERROR_FLOAT_FORMAT
            return
        }
        val splitPriceInt = _splitPrice.value ?: ERROR_INTEGER_FORMAT
        if (splitPriceInt <= 0) {
            _discountRate.value = ERROR_FLOAT_FORMAT
            return
        }
        val discountPrice = (eachPriceInt - splitPriceInt).toFloat()
        _discountRate.value = (discountPrice / eachPriceInt) * 100
        if (discountPrice < 0) _discountRate.value = 0f
    }

    fun increaseTotalCount() {
        var totalCountInt = totalCount.value?.toIntOrNull() ?: return
        totalCountInt++
        totalCount.value = totalCountInt.toString()
    }

    fun decreaseTotalCount() {
        var totalCountInt = totalCount.value?.toIntOrNull() ?: return
        totalCountInt--
        totalCount.value = totalCountInt.toString()
    }

    // memberId는 임시값을 보내고 있음!
    fun postOffering() {
        val memberId = BuildConfig.TOKEN.toLong()
        val title = title.value ?: return
        val totalCount = totalCount.value ?: return
        val totalPrice = totalPrice.value ?: return
        val meetingAddress = meetingAddress.value ?: return
        val meetingAddressDetail = meetingAddressDetail.value ?: return
        val deadline = deadline.value ?: return
        val description = description.value ?: return

        viewModelScope.launch {
            val totalCountConverted = makeTotalCountInvalidEvent(totalCount) ?: return@launch
            val totalPriceConverted = makeTotalPriceInvalidEvent(totalPrice) ?: return@launch

            var eachPriceNotBlank: Int? = 0
            runCatching {
                eachPriceNotBlank = returnNullIfBlank(eachPrice.value)
            }.onFailure {
                makeEachPriceInvalidEvent()
                return@launch
            }

            offeringWriteRepository.saveOffering(
                memberId = memberId,
                title = title,
                productUrl = productUrl.value,
                thumbnailUrl = thumbnailUrl.value,
                totalCount = totalCountConverted,
                totalPrice = totalPriceConverted,
                eachPrice = eachPriceNotBlank,
                meetingAddress = meetingAddress,
                meetingAddressDong = meetingAddressDong.value,
                meetingAddressDetail = meetingAddressDetail,
                deadline = deadline,
                description = description,
            ).onSuccess {
                Log.d("alsong", "success")
            }.onFailure {
                Log.e("alsong", it.message.toString())
            }
        }
    }

    private fun returnNullIfBlank(input: String?): Int? {
        val inputTrim = input?.trim()
        // 낱개 가격이 빈 문자열이면
        if (inputTrim.isNullOrBlank()) {
            return null
        }
        // 낱개 가격이 음수이거나 정수가 아니거나 수가 아니면
        if (inputTrim.toInt() < 0) {
            throw NumberFormatException()
        }
        // 낱개 가격이 양수이면
        return inputTrim.toInt()
    }

    private fun makeTotalCountInvalidEvent(totalCount: String): Int? {
        val totalCountConverted = totalCount.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalCountConverted < MINIMUM_TOTAL_COUNT || totalCountConverted > MAXIMUM_TOTAL_COUNT) {
            _invalidTotalCountEvent.setValue(true)
            return null
        }
        return totalCountConverted
    }

    private fun makeTotalPriceInvalidEvent(totalPrice: String): Int? {
        val totalPriceConverted = totalPrice.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalPriceConverted < 0) {
            _invalidTotalPriceEvent.setValue(true)
            return null
        }
        return totalPriceConverted
    }

    private fun makeEachPriceInvalidEvent() {
        _invalidEachPriceEvent.setValue(true)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val ERROR_FLOAT_FORMAT = -1f
        private const val MINIMUM_TOTAL_COUNT = 2
        private const val MAXIMUM_TOTAL_COUNT = 10_000

        @Suppress("UNCHECKED_CAST")
        fun getFactory(offeringWriteRepository: OfferingWriteRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return OfferingWriteViewModel(
                        offeringWriteRepository,
                    ) as T
                }
            }
    }
}
