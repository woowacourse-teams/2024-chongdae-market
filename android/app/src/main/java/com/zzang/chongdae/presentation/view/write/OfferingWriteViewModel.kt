package com.zzang.chongdae.presentation.view.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.repository.OfferingWriteRepository
import kotlinx.coroutines.launch

class OfferingWriteViewModel(
    private val offeringWriteRepository: OfferingWriteRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<OfferingWriteUiState> = MutableLiveData()
    val uiState: LiveData<OfferingWriteUiState> get() = _uiState

    private fun postOffering() {
        val memberId = _uiState.value?.memberId ?: return
        val title = _uiState.value?.title ?: return
        val totalCount = _uiState.value?.totalCount ?: return
        val totalPrice = _uiState.value?.totalPrice ?: return
        val eachPrice = _uiState.value?.eachPrice ?: return
        val meetingAddress = _uiState.value?.meetingAddress ?: return
        val meetingAddressDetail = _uiState.value?.meetingAddressDetail ?: return
        val deadline = _uiState.value?.deadline ?: return
        val description = _uiState.value?.description ?: return

        viewModelScope.launch {
            offeringWriteRepository.saveOffering(
                memberId = memberId,
                title = title,
                productUrl = _uiState.value?.productUrl,
                thumbnailUrl = _uiState.value?.thumbnailUrl,
                totalCount = totalCount,
                totalPrice = totalPrice,
                eachPrice = eachPrice,
                meetingAddress = meetingAddress,
                meetingAddressDong = _uiState.value?.meetingAddressDong,
                meetingAddressDetail = meetingAddressDetail,
                deadline = deadline,
                description = description,
            ).onSuccess {
                Log.d("alsong", "postOffering: 잘보내짐")
            }.onFailure {
                Log.d("error", it.message.toString())
            }
        }
    }

    companion object {
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
