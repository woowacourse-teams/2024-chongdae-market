package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.util.handleAccessTokenExpiration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CommentDetailViewModel(
    private val authRepository: AuthRepository,
    private val offeringId: Long,
    val offeringTitle: String,
    private val offeringRepository: OfferingRepository,
    private val commentDetailRepository: CommentDetailRepository,
) : ViewModel() {
    private var cachedComments: List<Comment> = emptyList()
    private val cachedMeetings: Meetings? = null
    private var pollJob: Job? = null
    val commentContent = MutableLiveData("")

    private val _isCollapsibleViewVisible = MutableLiveData(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

    private val _deadline = MutableLiveData<LocalDateTime>()
    val deadline: LiveData<LocalDateTime> get() = _deadline

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _locationDetail = MutableLiveData<String>()
    val locationDetail: LiveData<String> get() = _locationDetail

    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _offeringStatusButtonText = MutableLiveData<String>()
    val offeringStatusButtonText: LiveData<String> get() = _offeringStatusButtonText

    private val _offeringStatusImageUrl = MutableLiveData<String>()
    val offeringStatusImageUrl: LiveData<String> get() = _offeringStatusImageUrl

    private val _showStatusDialogEvent = MutableLiveData<Unit>()
    val showStatusDialogEvent: LiveData<Unit> get() = _showStatusDialogEvent

    private val _onBackPressedEvent = MutableSingleLiveData<Unit>()
    val onBackPressedEvent: SingleLiveData<Unit> get() = _onBackPressedEvent

    init {
        startPolling()
        updateStatusInfo()
        loadMeetings()
    }

    private fun updateStatusInfo() {
        viewModelScope.launch {
            offeringRepository.fetchOfferingStatus(offeringId).onSuccess {
                _offeringStatusButtonText.value = it.buttonText
                _offeringStatusImageUrl.value = it.imageUrl
            }.onFailure {
                Log.e("error", "updateStatusInfo: ${it.message}")
                handleAccessTokenExpiration(authRepository, it) { updateStatusInfo() }
            }
        }
    }

    fun updateOffering() {
        _showStatusDialogEvent.value = Unit
    }

    fun updateOfferingStatus() {
        viewModelScope.launch {
            offeringRepository.updateOfferingStatus(offeringId).onSuccess {
                updateStatusInfo()
            }.onFailure {
                Log.e("error", "updateOfferingStatus: ${it.message}")
                handleAccessTokenExpiration(authRepository, it) { updateOfferingStatus() }
            }
        }
    }

    fun loadComments() {
        viewModelScope.launch {
            commentDetailRepository.fetchComments(
                offeringId = offeringId,
            ).onSuccess { newComments ->
                if (newComments != cachedComments) {
                    cachedComments = newComments
                    _comments.value = newComments
                }
            }.onFailure {
                Log.e("error", "loadComments: ${it.message}")
                handleAccessTokenExpiration(authRepository, it) { loadComments() }
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

        viewModelScope.launch {
            commentDetailRepository.saveComment(
                offeringId = offeringId,
                comment = content,
            ).onSuccess {
                commentContent.value = ""
            }.onFailure {
                Log.e("error", "postComment: ${it.message}")
                handleAccessTokenExpiration(authRepository, it) { postComment() }
            }
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            commentDetailRepository.fetchMeetings(offeringId).onSuccess {
                if (it != cachedMeetings) {
                    _deadline.value = it.deadline
                    _location.value = it.meetingAddress
                    _locationDetail.value = it.meetingAddressDetail
                }
            }.onFailure {
                Log.e("error", "loadMeetings: ${it.message}")
                handleAccessTokenExpiration(authRepository, it) { loadMeetings() }
            }
        }
    }

    fun onBackClick() {
        _onBackPressedEvent.setValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            authRepository: AuthRepository,
            offeringId: Long,
            offeringTitle: String,
            offeringRepository: OfferingRepository,
            commentDetailRepository: CommentDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return CommentDetailViewModel(
                    authRepository,
                    offeringId,
                    offeringTitle,
                    offeringRepository,
                    commentDetailRepository,
                ) as T
            }
        }
    }
}
