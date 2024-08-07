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
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Count
import com.zzang.chongdae.domain.model.DiscountPrice
import com.zzang.chongdae.domain.model.Price
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class OfferingWriteViewModel(
    private val offeringRepository: OfferingRepository,
) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData("")

    val productUrl: MutableLiveData<String?> = MutableLiveData(null)

    val thumbnailUrl: MutableLiveData<String?> = MutableLiveData("")

    val deleteImageVisible: LiveData<Boolean> = thumbnailUrl.map { !it.isNullOrBlank() }

    val totalCount: MutableLiveData<String> = MutableLiveData("$MINIMUM_TOTAL_COUNT")

    val totalPrice: MutableLiveData<String> = MutableLiveData("")

    val eachPrice: MutableLiveData<String?> = MutableLiveData("")

    val meetingAddress: MutableLiveData<String> = MutableLiveData("")

    private val meetingAddressDong: MutableLiveData<String?> = MutableLiveData()

    val meetingAddressDetail: MutableLiveData<String> = MutableLiveData("")

    val deadline: MutableLiveData<String> = MutableLiveData("")

    private val deadlineValue: MutableLiveData<String> = MutableLiveData("")

    val description: MutableLiveData<String> = MutableLiveData("")

    private val _errorEvent: MutableSingleLiveData<Int> = MutableSingleLiveData()
    val errorEvent: SingleLiveData<Int> get() = _errorEvent

    private val _submitButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
    val submitButtonEnabled: LiveData<Boolean> get() = _submitButtonEnabled

    private val _extractButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
    val extractButtonEnabled: LiveData<Boolean> get() = _extractButtonEnabled

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

    private val _deadlineChoiceEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val deadlineChoiceEvent: SingleLiveData<Boolean> get() = _deadlineChoiceEvent

    private val _finishEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val finishEvent: SingleLiveData<Boolean> get() = _finishEvent

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

        _extractButtonEnabled.apply {
            addSource(productUrl) { value = !productUrl.value.isNullOrBlank() }
        }
    }

    private fun safeUpdateSplitPrice() {
        runCatching {
            updateSplitPrice()
        }.onFailure {
            _splitPrice.value = ERROR_INTEGER_FORMAT
        }
    }

    fun clearProductUrl() {
        productUrl.value = null
    }

    fun postProductImageOg() {
        viewModelScope.launch {
            offeringRepository.saveProductImageOg(productUrl.value ?: "").onSuccess {
                if (it.imageUrl.isNullOrBlank()) {
                    _errorEvent.setValue(R.string.error_empty_product_url)
                    return@launch
                }
                thumbnailUrl.value = HTTPS + it.imageUrl
            }.onFailure {
                Log.e("error", it.message.toString())
                _errorEvent.setValue(R.string.error_invalid_product_url)
            }
        }
    }

    fun clearProductImage() {
        thumbnailUrl.value = null
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

    fun makeDeadlineChoiceEvent() {
        _deadlineChoiceEvent.setValue(true)
    }

    fun updateDeadline(
        date: String,
        time: String,
    ) {
        val dateTime = "$date $time"
        val inputFormat = SimpleDateFormat(INPUT_DATE_TIME_FORMAT, Locale.KOREAN)
        val outputFormat = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT, Locale.getDefault())

        val parsedDateTime = inputFormat.parse(dateTime)
        deadlineValue.value = parsedDateTime?.let { outputFormat.format(it) }
        deadline.value = dateTime
    }

    // memberId는 임시값을 보내고 있음!
    fun postOffering() {
        val memberId = BuildConfig.TOKEN.toLong()
        val title = title.value ?: return
        val totalCount = totalCount.value ?: return
        val totalPrice = totalPrice.value ?: return
        val meetingAddress = meetingAddress.value ?: return
        val meetingAddressDetail = meetingAddressDetail.value ?: return
        val deadline = deadlineValue.value ?: return
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

            offeringRepository.saveOffering(
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
                makeFinishEvent()
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

    private fun makeFinishEvent() {
        _finishEvent.setValue(true)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val ERROR_FLOAT_FORMAT = -1f
        private const val MINIMUM_TOTAL_COUNT = 2
        private const val MAXIMUM_TOTAL_COUNT = 10_000
        private const val INPUT_DATE_TIME_FORMAT = "yyyy년 M월 d일 a h시 m분"
        private const val OUTPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        private const val HTTPS = "https:"

        @Suppress("UNCHECKED_CAST")
        fun getFactory(offeringRepository: OfferingRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return OfferingWriteViewModel(
                        offeringRepository,
                    ) as T
                }
            }
    }
}