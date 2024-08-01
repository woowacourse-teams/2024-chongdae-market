package com.zzang.chongdae.presentation.view.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.repository.OfferingWriteRepository
import kotlinx.coroutines.launch

class OfferingWriteViewModel(
    private val offeringWriteRepository: OfferingWriteRepository,
) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData()

    val productUrl: MutableLiveData<String?> = MutableLiveData()

    val thumbnailUrl: MutableLiveData<String?> = MutableLiveData()

    val totalCount: MutableLiveData<String> = MutableLiveData()

    val totalPrice: MutableLiveData<String> = MutableLiveData()

    val eachPrice: MutableLiveData<String?> = MutableLiveData()

    val meetingAddress: MutableLiveData<String> = MutableLiveData()

    private val _meetingAddressDong: MutableLiveData<String?> = MutableLiveData()
    val meetingAddressDong: LiveData<String?> get() = _meetingAddressDong

    val meetingAddressDetail: MutableLiveData<String> = MutableLiveData()

    val deadline: MutableLiveData<String> = MutableLiveData()

    val description: MutableLiveData<String> = MutableLiveData()

    // memberId는 임시값을 보내고 있음!
    fun postOffering() {
        Log.d("alsong", "======================================")
        Log.d("alsong", "title: ${title.value}")
        Log.d("alsong", "productUrl: ${productUrl.value}")
        Log.d("alsong", "thumbnailUrl: ${thumbnailUrl.value}")
        Log.d("alsong", "totalCount: ${totalCount.value}")
        Log.d("alsong", "totalPrice: ${totalPrice.value}")
        Log.d("alsong", "eachPrice: ${eachPrice.value}")
        Log.d("alsong", "meetingAddress: ${meetingAddress.value}")
        Log.d("alsong", "meetingAddressDetail: ${meetingAddressDetail.value}")
        Log.d("alsong", "deadline: ${deadline.value}")
        Log.d("alsong", "description: ${description.value}")

        val memberId = BuildConfig.TOKEN.toLong()
        val title = title.value ?: return
        val totalCount = totalCount.value ?: return
        val totalPrice = totalPrice.value ?: return
        val meetingAddress = meetingAddress.value ?: return
        val meetingAddressDetail = meetingAddressDetail.value ?: return
        val deadline = deadline.value ?: return
        val description = description.value ?: return

        viewModelScope.launch {
            offeringWriteRepository.saveOffering(
                memberId = memberId,
                title = title,
                productUrl = productUrl.value,
                thumbnailUrl = thumbnailUrl.value,
                totalCount = totalCount.toIntOrNull() ?: -1,
                totalPrice = totalPrice.toIntOrNull() ?: -1,
                eachPrice = eachPrice.value?.toIntOrNull(),
                meetingAddress = meetingAddress,
                meetingAddressDong = meetingAddressDong.value,
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
