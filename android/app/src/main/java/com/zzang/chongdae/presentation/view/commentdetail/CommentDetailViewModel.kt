package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.OfferingStatus
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> get() = _comments

    private var cachedComments: List<Comment> = emptyList()
    private var pollJob: Job? = null

    private val _offeringStatus = MutableLiveData(OfferingStatus.Recruiting)
    val offeringStatus: LiveData<OfferingStatus> get() = _offeringStatus

    init {
        startPolling()
    }

    fun updateStatus() {
        _offeringStatus.value = _offeringStatus.value?.let { OfferingStatus.nextStatus(it) }
    }

    private fun loadComments() {
        viewModelScope.launch {
            commentDetailRepository.fetchComments(
                offeringId = offeringId,
                memberId = BuildConfig.TOKEN.toLong(),
            ).onSuccess { newComments ->
                if (newComments != cachedComments) {
                    cachedComments = newComments
                    _comments.value = newComments
                }
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    private fun startPolling() {
        pollJob?.cancel()
        pollJob =
            viewModelScope.launch {
                while (true) {
                    loadComments()
                    delay(1000)
                }
            }
    }

    private fun stopPolling() {
        pollJob?.cancel()
    }

    fun toggleCollapsibleView() {
        _isCollapsibleViewVisible.value = _isCollapsibleViewVisible.value?.not()
        if (_isCollapsibleViewVisible.value == true) {
            loadMeetings()
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

    override fun onCleared() {
        super.onCleared()
        stopPolling()
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
                return CommentDetailViewModel(
                    offeringId,
                    offeringTitle,
                    commentDetailRepository,
                ) as T
            }
        }
    }
}
