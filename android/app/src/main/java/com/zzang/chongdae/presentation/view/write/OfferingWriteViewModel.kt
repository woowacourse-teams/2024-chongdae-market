package com.zzang.chongdae.presentation.view.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.model.Count
import com.zzang.chongdae.domain.model.DiscountPrice
import com.zzang.chongdae.domain.model.Price
import com.zzang.chongdae.domain.repository.OfferingsRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch

class OfferingWriteViewModel(
    private val offeringsRepository: OfferingsRepository,
) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData("")

    val productUrl: MutableLiveData<String?> = MutableLiveData("")

    val thumbnailUrl: MutableLiveData<String?> = MutableLiveData("")

    val totalCount: MutableLiveData<String> = MutableLiveData("${MINIMUM_TOTAL_COUNT}")

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
    val splitPriceVisibility: LiveData<Boolean>
        get() = _splitPrice.map { it >= 0 }

    val discountRateVisibility: LiveData<Boolean>
        get() = _discountRate.map { it >= 0 }

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
            addSource(totalCount) { safeUpdateSplitPrice() }
            addSource(totalPrice) { safeUpdateSplitPrice() }
        }

        _discountRate.apply {
            addSource(_splitPrice) { safeUpdateDiscountRate() }
            addSource(eachPrice) { safeUpdateDiscountRate() }
        }
    }

    private fun safeUpdateSplitPrice() {
        runCatching {
            updateSplitPrice()
        }.onFailure {
            _splitPrice.value = ERROR_INTEGER_FORMAT
        }
    }

    private fun safeUpdateDiscountRate() {
        runCatching {
            updateDiscountRate()
        }.onFailure {
            _discountRate.value = ERROR_FLOAT_FORMAT
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
        val totalPrice = Price.fromString(totalPrice.value)
        val totalCount = Count.fromString(totalCount.value)
        _splitPrice.value = totalPrice.amount / totalCount.number
    }

    private fun updateDiscountRate() {
        val eachPrice = Price.fromString(eachPrice.value)
        val splitPrice = Price.fromInteger(_splitPrice.value)
        val discountPriceValue = eachPrice.amount - splitPrice.amount
        val discountPrice = DiscountPrice.fromFloat(discountPriceValue.toFloat())
        _discountRate.value = (discountPrice.amount / eachPrice.amount) * 100
    }

    fun increaseTotalCount() {
        val totalCount = Count.fromString(totalCount.value).increase()
        this.totalCount.value = totalCount.number.toString()
    }

    fun decreaseTotalCount() {
        val totalCount = Count.fromString(totalCount.value).decrease()
        this.totalCount.value = totalCount.number.toString()
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
                eachPriceNotBlank = eachPriceToPositiveIntOrNull(eachPrice.value)
            }.onFailure {
                makeEachPriceInvalidEvent()
                return@launch
            }

            offeringsRepository.saveOffering(
                uiModel =
                    OfferingWriteUiModel(
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
                    ),
            ).onSuccess {
                Log.d("alsong", "success")
            }.onFailure {
                Log.e("alsong", it.message.toString())
            }
        }
    }

    private fun eachPriceToPositiveIntOrNull(input: String?): Int? {
        val eachPriceInputTrim = input?.trim()
        if (eachPriceInputTrim.isNullOrBlank()) {
            return null
        }
        if (eachPriceInputTrim.toInt() < 0) {
            throw NumberFormatException()
        }
        return eachPriceInputTrim.toInt()
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
        fun getFactory(offeringsRepository: OfferingsRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return OfferingWriteViewModel(
                        offeringsRepository,
                    ) as T
                }
            }
    }
}
