package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CommentDetailViewModel(
    private val offeringId: Long,
    val offeringTitle: String,
    private val commentDetailRepository: CommentDetailRepository,
) : ViewModel() {
    val commentContent = MutableLiveData<String>("")

    private val _isCollapsibleViewVisible = MutableLiveData<Boolean>(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

    private val _deadline = MutableLiveData<LocalDateTime>()
    val deadline: LiveData<LocalDateTime> get() = _deadline

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _locationDetail = MutableLiveData<String>()
    val locationDetail: LiveData<String> get() = _locationDetail

    fun toggleCollapsibleView() {
        _isCollapsibleViewVisible.value = _isCollapsibleViewVisible.value?.not()
        if (_isCollapsibleViewVisible.value == true) {
            loadMeetings()
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            commentDetailRepository.getMeetings(offeringId).onSuccess {
                _deadline.value = it.deadline
                _location.value = it.meetingAddress
                _locationDetail.value = it.meetingAddressDetail
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    fun postComment() {
        val content = commentContent.value?.trim()
        if (content.isNullOrEmpty()) {
            return
        }

        // memberId를 가져오는 로직 수정 예정(로그인 기능 구현 이후)
        viewModelScope.launch {
            commentDetailRepository.saveParticipation(
                memberId = BuildConfig.TOKEN.toLong(),
                offeringId = offeringId,
                comment = content,
            ).onSuccess {
                commentContent.value = ""
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
            ): T {
                return CommentDetailViewModel(offeringId, offeringTitle, commentDetailRepository) as T
            }
        }
    }
}
