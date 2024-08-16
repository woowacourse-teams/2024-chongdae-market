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
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Count
import com.zzang.chongdae.domain.model.DiscountPrice
import com.zzang.chongdae.domain.model.Price
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    val originPrice: MutableLiveData<String?> = MutableLiveData("")

    val meetingAddress: MutableLiveData<String> = MutableLiveData("")

    private val meetingAddressDong: MutableLiveData<String?> = MutableLiveData()

    val meetingAddressDetail: MutableLiveData<String> = MutableLiveData("")

    val meetingDate: MutableLiveData<String> = MutableLiveData("")

    private val meetingDateValue: MutableLiveData<String> = MutableLiveData("")

    val description: MutableLiveData<String> = MutableLiveData("")

    val descriptionLength: LiveData<Int>
        get() = description.map { it.length }

    private val _essentialSubmitButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
    val essentialSubmitButtonEnabled: LiveData<Boolean> get() = _essentialSubmitButtonEnabled

    private val _extractButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
    val extractButtonEnabled: LiveData<Boolean> get() = _extractButtonEnabled

    private val _splitPrice: MediatorLiveData<Int> = MediatorLiveData(ERROR_INTEGER_FORMAT)
    val splitPrice: LiveData<Int> get() = _splitPrice

    private val _discountRate: MediatorLiveData<Float> = MediatorLiveData(ERROR_FLOAT_FORMAT)
    val splitPriceValidity: LiveData<Boolean>
        get() = _splitPrice.map { it >= 0 }

    val discountRateValidity: LiveData<Boolean>
        get() = _discountRate.map { it >= 0 }

    val discountRate: LiveData<Float> get() = _discountRate

    private val _meetingDateChoiceEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val meetingDateChoiceEvent: SingleLiveData<Boolean> get() = _meetingDateChoiceEvent

    private val _navigateToOptionalEvent: MutableSingleLiveData<Boolean> = MutableSingleLiveData()
    val navigateToOptionalEvent: SingleLiveData<Boolean> get() = _navigateToOptionalEvent

    private val _submitOfferingEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
    val submitOfferingEvent: SingleLiveData<Unit> get() = _submitOfferingEvent

    private val _imageUploadEvent = MutableLiveData<Unit>()
    val imageUploadEvent: LiveData<Unit> get() = _imageUploadEvent

    private val _writeUIState = MutableLiveData<WriteUIState>(WriteUIState.Initial)
    val writeUIState: LiveData<WriteUIState> get() = _writeUIState

    val isLoading: LiveData<Boolean> = _writeUIState.map { it is WriteUIState.Loading }

    init {
        _essentialSubmitButtonEnabled.apply {
            addSource(title) { updateSubmitButtonEnabled() }
            addSource(totalCount) { updateSubmitButtonEnabled() }
            addSource(totalPrice) { updateSubmitButtonEnabled() }
            addSource(meetingAddress) { updateSubmitButtonEnabled() }
            addSource(meetingDate) { updateSubmitButtonEnabled() }
        }

        _splitPrice.apply {
            addSource(totalCount) { safeUpdateSplitPrice() }
            addSource(totalPrice) { safeUpdateSplitPrice() }
        }

        _discountRate.apply {
            addSource(_splitPrice) { safeUpdateDiscountRate() }
            addSource(originPrice) { safeUpdateDiscountRate() }
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

    fun onUploadPhotoClick() {
        _imageUploadEvent.value = Unit
    }

    fun uploadImageFile(multipartBody: MultipartBody.Part) {
        viewModelScope.launch {
            _writeUIState.value = WriteUIState.Loading
            offeringRepository.saveProductImageS3(multipartBody).onSuccess {
                _writeUIState.value = WriteUIState.Success(it.imageUrl)
                thumbnailUrl.value = it.imageUrl
            }.onFailure {
                Log.e("error", it.message.toString())
                _writeUIState.value =
                    WriteUIState.Error(R.string.error_invalid_product_url, it.message.toString())
            }
        }
    }

    fun postProductImageOg() {
        viewModelScope.launch {
            _writeUIState.value = WriteUIState.Loading
            offeringRepository.saveProductImageOg(productUrl.value ?: "").onSuccess {
                if (it.imageUrl.isBlank()) {
                    _writeUIState.value = WriteUIState.Empty(R.string.error_empty_product_url)
                    return@launch
                }
                thumbnailUrl.value = HTTPS + it.imageUrl
            }.onFailure {
                Log.e("error", it.message.toString())
                _writeUIState.value =
                    WriteUIState.Error(R.string.error_invalid_product_url, it.message.toString())
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
        _essentialSubmitButtonEnabled.value = !title.value.isNullOrBlank() &&
            !totalCount.value.isNullOrBlank() &&
            !totalPrice.value.isNullOrBlank() &&
            !meetingAddress.value.isNullOrBlank() &&
            !meetingDate.value.isNullOrBlank()
    }

    private fun updateSplitPrice() {
        val totalPrice = Price.fromString(totalPrice.value)
        val totalCount = Count.fromString(totalCount.value)
        _splitPrice.value = totalPrice.amount / totalCount.number
    }

    private fun updateDiscountRate() {
        val originPrice = Price.fromString(originPrice.value)
        val splitPrice = Price.fromInteger(_splitPrice.value)
        val discountPriceValue = originPrice.amount - splitPrice.amount
        val discountPrice = DiscountPrice.fromFloat(discountPriceValue.toFloat())
        _discountRate.value = (discountPrice.amount / originPrice.amount) * 100
    }

    fun increaseTotalCount() {
        val totalCount = Count.fromString(totalCount.value).increase()
        this.totalCount.value = totalCount.number.toString()
    }

    fun decreaseTotalCount() {
        if (Count.fromString(totalCount.value).number < 0) {
            this.totalCount.value = MINIMUM_TOTAL_COUNT.toString()
            return
        }
        val totalCount = Count.fromString(totalCount.value).decrease()
        this.totalCount.value = totalCount.number.toString()
    }

    fun makeMeetingDateChoiceEvent() {
        _meetingDateChoiceEvent.setValue(true)
    }

    fun updateMeetingDate(date: String) {
        val dateTime = "$date"
        val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.KOREAN)
        val outputFormat = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT, Locale.getDefault())

        val parsedDateTime = inputFormat.parse(dateTime)
        meetingDateValue.value = parsedDateTime?.let { outputFormat.format(it) }
        meetingDate.value = dateTime
    }

    fun postOffering() {
        val title = title.value ?: return
        val totalCount = totalCount.value ?: return
        val totalPrice = totalPrice.value ?: return
        val meetingAddress = meetingAddress.value ?: return
        val meetingAddressDetail = meetingAddressDetail.value ?: return
        val meetingDate = meetingDateValue.value ?: return
        val description = description.value ?: return

        val totalCountConverted = makeTotalCountInvalidEvent(totalCount) ?: return
        val totalPriceConverted = makeTotalPriceInvalidEvent(totalPrice) ?: return

        var originPriceNotBlank: Int? = 0
        runCatching {
            originPriceNotBlank = originPriceToPositiveIntOrNull(originPrice.value)
        }.onFailure {
            makeOriginPriceInvalidEvent()
            return
        }
        if (isOriginPriceCheaperThanSplitPriceEvent()) return

        viewModelScope.launch {
            offeringRepository.saveOffering(
                uiModel =
                    OfferingWriteUiModel(
                        title = title,
                        productUrl = productUrl.value,
                        thumbnailUrl = thumbnailUrl.value,
                        totalCount = totalCountConverted,
                        totalPrice = totalPriceConverted,
                        originPrice = originPriceNotBlank,
                        meetingAddress = meetingAddress,
                        meetingAddressDong = meetingAddressDong.value,
                        meetingAddressDetail = meetingAddressDetail,
                        meetingDate = meetingDate,
                        description = description,
                    ),
            ).onSuccess {
                makeSubmitOfferingEvent()
            }.onFailure {
                Log.e("error", it.message.toString())
                _writeUIState.value =
                    WriteUIState.Error(R.string.write_error_writing, it.message.toString())
            }
        }
    }

    private fun originPriceToPositiveIntOrNull(input: String?): Int? {
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
        val totalCountValue = totalCount.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalCountValue < MINIMUM_TOTAL_COUNT || totalCountValue > MAXIMUM_TOTAL_COUNT) {
            _writeUIState.value = WriteUIState.InvalidInput(R.string.write_invalid_total_count)
            return null
        }
        return totalCountValue
    }

    private fun makeTotalPriceInvalidEvent(totalPrice: String): Int? {
        val totalPriceConverted = totalPrice.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
        if (totalPriceConverted < 0) {
            _writeUIState.value = WriteUIState.InvalidInput(R.string.write_invalid_total_price)
            return null
        }
        return totalPriceConverted
    }

    private fun makeOriginPriceInvalidEvent() {
        _writeUIState.value = WriteUIState.InvalidInput(R.string.write_invalid_each_price)
    }

    private fun isOriginPriceCheaperThanSplitPriceEvent(): Boolean {
        if (originPrice.value.isNullOrBlank()) return false
        val discountRateValue = discountRate.value ?: ERROR_FLOAT_FORMAT
        if (discountRateValue <= 0f) {
            _writeUIState.value =
                WriteUIState.InvalidInput(R.string.write_origin_price_cheaper_than_total_price)
            return true
        }
        return false
    }

    fun makeNavigateToOptionalEvent() {
        _navigateToOptionalEvent.setValue(true)
    }

    private fun makeSubmitOfferingEvent() {
        _submitOfferingEvent.setValue(Unit)
    }

    fun initOfferingWriteInputs() {
        title.value = ""
        productUrl.value = ""
        thumbnailUrl.value = ""
        totalCount.value = "$MINIMUM_TOTAL_COUNT"
        totalPrice.value = ""
        originPrice.value = ""
        meetingAddress.value = ""
        meetingAddressDetail.value = ""
        meetingDate.value = ""
        meetingDateValue.value = ""
        description.value = ""
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val ERROR_FLOAT_FORMAT = -1f
        private const val MINIMUM_TOTAL_COUNT = 2
        private const val MAXIMUM_TOTAL_COUNT = 10_000
        private const val INPUT_DATE_TIME_FORMAT = "yyyy년 M월 d일 a h시 m분"
        private const val INPUT_DATE_FORMAT = "yyyy년 M월 d일"
        private const val OUTPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val HTTPS = "https:"

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
