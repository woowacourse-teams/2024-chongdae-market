package com.zzang.chongdae.presentation.view.offeringmodify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.R
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.FetchOfferingDetailUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostOfferingModifyUseCaseQualifier
import com.zzang.chongdae.di.annotations.PostProductImageOgUseCaseQualifier
import com.zzang.chongdae.di.annotations.UploadImageFileUseCaseQualifier
import com.zzang.chongdae.domain.model.Count
import com.zzang.chongdae.domain.model.DiscountPrice
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.model.Price
import com.zzang.chongdae.domain.usecase.offeringmodify.FetchOfferingDetailUseCase
import com.zzang.chongdae.domain.usecase.offeringmodify.PostOfferingModifyUseCase
import com.zzang.chongdae.domain.usecase.write.PostProductImageOgUseCase
import com.zzang.chongdae.domain.usecase.write.UploadImageFileUseCase
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OfferingModifyViewModel
    @Inject
    constructor(
        @UploadImageFileUseCaseQualifier private val uploadImageFileUseCase: UploadImageFileUseCase,
        @PostProductImageOgUseCaseQualifier private val postProductImageOgUseCase: PostProductImageOgUseCase,
        @FetchOfferingDetailUseCaseQualifier private val fetchOfferingDetailUseCase: FetchOfferingDetailUseCase,
        @PostOfferingModifyUseCaseQualifier private val postOfferingModifyUseCase: PostOfferingModifyUseCase,
    ) : ViewModel() {
        private var offeringId: Long = DEFAULT_OFFERING_ID

        val title: MutableLiveData<String> = MutableLiveData("")

        val productUrl: MutableLiveData<String?> = MutableLiveData(null)

        val thumbnailUrl: MutableLiveData<String?> = MutableLiveData("")

        val deleteImageVisible: LiveData<Boolean> = thumbnailUrl.map { !it.isNullOrBlank() }

        val totalCount: MutableLiveData<String> = MutableLiveData("$MINIMUM_TOTAL_COUNT")

        val totalPrice: MutableLiveData<String> = MutableLiveData("")

        val originPrice: MutableLiveData<String?> = MutableLiveData("")

        val meetingAddress: MutableLiveData<String> = MutableLiveData("")

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

        private val _submitOfferingModifyEvent: MutableSingleLiveData<Unit> = MutableSingleLiveData()
        val submitOfferingModifyEvent: SingleLiveData<Unit> get() = _submitOfferingModifyEvent

        private val _imageUploadEvent = MutableLiveData<Unit>()
        val imageUploadEvent: LiveData<Unit> get() = _imageUploadEvent

        private val _modifyUIState = MutableLiveData<ModifyUIState>(ModifyUIState.Initial)
        val modifyUIState: LiveData<ModifyUIState> get() = _modifyUIState

        val isLoading: LiveData<Boolean> = _modifyUIState.map { it is ModifyUIState.Loading }

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

        fun initOfferingId(id: Long) {
            offeringId = id
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
                _modifyUIState.value = ModifyUIState.Loading
                when (val result = uploadImageFileUseCase.invoke(multipartBody)) {
                    is Result.Success -> {
                        _modifyUIState.value = ModifyUIState.Success(result.data.imageUrl)
                        thumbnailUrl.value = result.data.imageUrl
                    }

                    is Result.Error -> {
                        Log.e("error", "uploadImageFile: ${result.error}")
                        _modifyUIState.value =
                            ModifyUIState.Error(R.string.all_error_image_upload, "${result.error}")
                    }
                }
            }
        }

        fun postProductImageOg() {
            viewModelScope.launch {
                _modifyUIState.value = ModifyUIState.Loading
                when (val result = postProductImageOgUseCase.invoke(productUrl.value ?: "")) {
                    is Result.Success -> {
                        if (result.data.imageUrl.isBlank()) {
                            _modifyUIState.value = ModifyUIState.Empty(R.string.error_empty_product_url)
                            return@launch
                        }
                        _modifyUIState.value = ModifyUIState.Success(result.data.imageUrl)
                        thumbnailUrl.value = HTTPS + result.data.imageUrl
                    }

                    is Result.Error -> {
                        Log.e("error", "postProductImageOg: ${result.error}")
                        _modifyUIState.value =
                            ModifyUIState.Error(R.string.error_invalid_product_url, "${result.error}")
                    }
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
            val totalCount = Count.fromString(totalCount.value, MINIMUM_TOTAL_COUNT)
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
            val totalCount = Count.fromString(totalCount.value, MINIMUM_TOTAL_COUNT).increase()
            this.totalCount.value = totalCount.number.toString()
        }

        fun decreaseTotalCount() {
            val totalCount = Count.fromString(totalCount.value, MINIMUM_TOTAL_COUNT)
            if (totalCount.number < 0) {
                this.totalCount.value = MINIMUM_TOTAL_COUNT.toString()
                return
            }
            val count = totalCount.decrease(MINIMUM_TOTAL_COUNT)
            this.totalCount.value = count.number.toString()
        }

        fun makeMeetingDateChoiceEvent() {
            _meetingDateChoiceEvent.setValue(true)
        }

        fun updateMeetingDate(date: String) {
            val dateTime = "$date"
            val inputFormat = SimpleDateFormat(DATE_FORMAT_DOMAIN, Locale.KOREAN)
            val outputFormat = SimpleDateFormat(DATE_TIME_FORMAT_REMOTE_WITH_SEC, Locale.getDefault())

            val parsedDateTime = inputFormat.parse(dateTime)
            meetingDateValue.value = parsedDateTime?.let { outputFormat.format(it) }
            meetingDate.value = dateTime
        }

        private fun initDateTimeWhenModify(dateTimeString: String) {
            val inputFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_REMOTE)
            val outputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DOMAIN)
            val inputFormatterWithSec = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_REMOTE_WITH_SEC)
            val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
            meetingDate.value = dateTime.format(outputFormatter)
            meetingDateValue.value = dateTime.format(inputFormatterWithSec)
        }

        fun fetchOfferingDetail() {
            viewModelScope.launch {
                when (val result = fetchOfferingDetailUseCase(offeringId)) {
                    is Result.Success -> {
                        initExistingOffering(result.data)
                    }

                    is Result.Error -> Log.e("error", "loadOffering Error: ${result.error.name}")
                }
            }
        }

        private fun initExistingOffering(offeringDetail: OfferingDetail) {
            title.value = offeringDetail.title
            productUrl.value = offeringDetail.productUrl
            thumbnailUrl.value = offeringDetail.thumbnailUrl
            totalCount.value = offeringDetail.totalCount.toString()
            totalPrice.value = offeringDetail.totalPrice.toString()
            originPrice.value = offeringDetail.originPrice?.toString() ?: ""
            meetingAddress.value = offeringDetail.meetingAddress
            meetingAddressDetail.value = offeringDetail.meetingAddressDetail
            initDateTimeWhenModify(offeringDetail.meetingDate.toString())
            description.value = offeringDetail.description
        }

        fun postOfferingModify() {
            val title = title.value ?: return
            val totalCount = totalCount.value ?: return
            val totalPrice = totalPrice.value ?: return
            val meetingAddress = meetingAddress.value ?: return
            val meetingAddressDetail = meetingAddressDetail.value ?: return
            val meetingDate = meetingDateValue.value ?: return
            val description = description.value ?: return

            val totalCountConverted = makeTotalCountInvalidEvent(totalCount) ?: return
            val totalPriceConverted = makeTotalPriceInvalidEvent(totalPrice) ?: return
            val meetingAddressDong = extractDong(meetingAddress)

            var originPriceNotBlank: Int? = 0
            runCatching {
                originPriceNotBlank = originPriceToPositiveIntOrNull(originPrice.value)
            }.onFailure {
                makeOriginPriceInvalidEvent()
                return
            }
            if (isOriginPriceCheaperThanSplitPriceEvent()) return

            viewModelScope.launch {
                when (
                    val result =
                        postOfferingModifyUseCase(
                            offeringId = offeringId,
                            offeringModifyDomainRequest =
                                OfferingModifyDomainRequest(
                                    title = title,
                                    productUrl = productUrlOrNull(),
                                    thumbnailUrl = thumbnailUrl.value,
                                    totalCount = totalCountConverted,
                                    totalPrice = totalPriceConverted,
                                    originPrice = originPriceNotBlank,
                                    meetingAddress = meetingAddress,
                                    meetingAddressDong = meetingAddressDong,
                                    meetingAddressDetail = meetingAddressDetail,
                                    meetingDate = meetingDate,
                                    description = description,
                                ),
                        )
                ) {
                    is Result.Success -> {
                        makeSubmitOfferingModifyEvent()
                    }

                    is Result.Error -> {
                        Log.e("error", "postOffering: ${result.error}")
                        _modifyUIState.value =
                            ModifyUIState.Error(R.string.modify_error_modifing, "${result.error}")
                    }
                }
            }
        }

        private fun productUrlOrNull(): String? {
            val productUrl = productUrl.value
            if (productUrl == "") return null
            return productUrl
        }

        private fun originPriceToPositiveIntOrNull(input: String?): Int? {
            val originPriceInputTrim = input?.trim()
            if (originPriceInputTrim.isNullOrBlank()) {
                return null
            }
            if (originPriceInputTrim.toInt() < 0) {
                throw NumberFormatException()
            }
            return originPriceInputTrim.toInt()
        }

        private fun extractDong(address: String): String? {
            val regex = """\((.*?)\)""".toRegex()
            val matchResult = regex.find(address)
            val content = matchResult?.groups?.get(1)?.value
            return content?.split(",")?.get(0)?.trim()
        }

        private fun makeTotalCountInvalidEvent(totalCount: String): Int? {
            val totalCountValue = totalCount.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (totalCountValue < MINIMUM_TOTAL_COUNT || totalCountValue > MAXIMUM_TOTAL_COUNT) {
                _modifyUIState.value = ModifyUIState.InvalidInput(R.string.write_invalid_total_count)
                return null
            }
            return totalCountValue
        }

        private fun makeTotalPriceInvalidEvent(totalPrice: String): Int? {
            val totalPriceConverted = totalPrice.trim().toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (totalPriceConverted < 0) {
                _modifyUIState.value = ModifyUIState.InvalidInput(R.string.write_invalid_total_price)
                return null
            }
            return totalPriceConverted
        }

        private fun makeOriginPriceInvalidEvent() {
            _modifyUIState.value = ModifyUIState.InvalidInput(R.string.write_invalid_origin_price)
        }

        private fun isOriginPriceCheaperThanSplitPriceEvent(): Boolean {
            if (originPrice.value.isNullOrBlank()) return false
            val discountRateValue = discountRate.value ?: ERROR_FLOAT_FORMAT
            if (discountRateValue <= 0f) {
                _modifyUIState.value =
                    ModifyUIState.InvalidInput(R.string.write_origin_price_cheaper_than_total_price)
                return true
            }
            return false
        }

        fun makeNavigateToOptionalEvent() {
            _navigateToOptionalEvent.setValue(true)
        }

        private fun makeSubmitOfferingModifyEvent() {
            _submitOfferingModifyEvent.setValue(Unit)
        }

        fun initOfferingModifyInputs() {
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
            private const val DEFAULT_OFFERING_ID = 0L
            private const val ERROR_INTEGER_FORMAT = -1
            private const val ERROR_FLOAT_FORMAT = -1f
            private const val MINIMUM_TOTAL_COUNT = 2
            private const val MAXIMUM_TOTAL_COUNT = 10_000
            private const val INPUT_DATE_TIME_FORMAT = "yyyy년 M월 d일 a h시 m분"
            private const val DATE_FORMAT_DOMAIN = "yyyy년 M월 d일"
            private const val DATE_TIME_FORMAT_REMOTE = "yyyy-MM-dd'T'HH:mm"
            private const val DATE_TIME_FORMAT_REMOTE_WITH_SEC = "yyyy-MM-dd'T'HH:mm:ss"
            const val HTTPS = "https:"
        }
    }
