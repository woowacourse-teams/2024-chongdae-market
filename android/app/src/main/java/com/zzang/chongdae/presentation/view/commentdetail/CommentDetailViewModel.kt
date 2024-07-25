package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CommentDetailViewModel(
    private val offeringId: Long,
    val offeringTitle: String,
    private val commentDetailRepository: CommentDetailRepository,
) : ViewModel() {
    
    private val _isCollapsibleViewVisible = MutableLiveData<Boolean>(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

    private val _deadlineText = MutableLiveData<LocalDateTime>()
    val deadlineText: LiveData<LocalDateTime> get() = _deadlineText

    private val _locationText = MutableLiveData<String>()
    val locationText: LiveData<String> get() = _locationText

    private val _locationDetailText = MutableLiveData<String>()
    val locationDetailText: LiveData<String> get() = _locationDetailText

    fun toggleCollapsibleView() {
        _isCollapsibleViewVisible.value = _isCollapsibleViewVisible.value?.not()
        if (_isCollapsibleViewVisible.value == true) {
            loadMeetings()
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            commentDetailRepository.getMeetings(
                offeringId = offeringId,
            ).onSuccess {
                _deadlineText.value = it.deadline
                _locationText.value = it.meetingAddress
                _locationDetailText.value = it.meetingAddressDetail
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            offeringTitle: String,
            commentDetailRepository: CommentDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T = CommentDetailViewModel(offeringId, offeringTitle, commentDetailRepository) as T
        }
    }
}
