package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ProposerUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentDetailViewModel(
    private val offeringId: Long,
    private val offeringRepository: OfferingRepository,
    private val participantRepository: ParticipantRepository,
    private val commentDetailRepository: CommentDetailRepository,
) : ViewModel() {
    private var cachedComments: List<Comment> = emptyList()
    private var pollJob: Job? = null
    val commentContent = MutableLiveData("")

    private val _isCollapsibleViewVisible = MutableLiveData(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

    private val _meetings = MutableLiveData<MeetingsUiModel>()
    val meetings: LiveData<MeetingsUiModel> get() = _meetings

    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _commentOfferingInfo = MutableLiveData<CommentOfferingInfoUiModel>()
    val commentOfferingInfo: LiveData<CommentOfferingInfoUiModel> get() = _commentOfferingInfo

    private val _participants = MutableLiveData<ParticipantsUiModel>()
    val participants: LiveData<ParticipantsUiModel> get() = _participants

    private val _showStatusDialogEvent = MutableLiveData<Unit>()
    val showStatusDialogEvent: LiveData<Unit> get() = _showStatusDialogEvent

    private val _onExitOfferingEvent = MutableSingleLiveData<Unit>()
    val onExitOfferingEvent: SingleLiveData<Unit> get() = _onExitOfferingEvent

    private val _onBackPressedEvent = MutableSingleLiveData<Unit>()
    val onBackPressedEvent: SingleLiveData<Unit> get() = _onBackPressedEvent

    init {
        startPolling()
        updateCommentInfo()
        loadMeetings()
        loadParticipants()
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

    private fun updateCommentInfo() {
        viewModelScope.launch {
            commentDetailRepository.fetchCommentOfferingInfo(offeringId).onSuccess {
               _commentOfferingInfo.value = it.toUiModel()
            }.onFailure {
                Log.e("error", "updateStatusInfo: ${it.message}")
            }
        }
    }

    fun updateOfferingEvent() {
        _showStatusDialogEvent.value = Unit
    }

    fun updateOfferingStatus() {
        viewModelScope.launch {
            commentDetailRepository.updateOfferingStatus(offeringId).onSuccess {
                updateCommentInfo()
            }.onFailure {
                Log.e("error", "updateOfferingStatus: ${it.message}")
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
            }
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
            }
        }
    }

    fun toggleCollapsibleView() {
        _isCollapsibleViewVisible.value = _isCollapsibleViewVisible.value?.not()
        if (_isCollapsibleViewVisible.value == true) {
            loadMeetings()
        }
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            participantRepository.fetchParticipants(offeringId).onSuccess { participantsResponse ->
                _participants.value =
                    ParticipantsUiModel(
                        proposer = ProposerUiModel(nickname = participantsResponse.proposer.nickname),
                        participants =
                            participantsResponse.participants.map {
                                ParticipantUiModel(
                                    nickname = it.nickname,
                                )
                            },
                        totalCount = participantsResponse.participantCount.totalCount,
                        currentCount = participantsResponse.participantCount.currentCount,
                        price = participantsResponse.price,
                    )
            }.onFailure {
                Log.e("error", "loadParticipants: ${it.message}")
            }
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            offeringRepository.fetchMeetings(offeringId).onSuccess { meetings ->
                _meetings.value =
                    MeetingsUiModel(
                        meetingDate = meetings.meetingDate,
                        meetingAddress = meetings.meetingAddress,
                        meetingAddressDetail = meetings.meetingAddressDetail,
                    )
            }.onFailure {
                Log.e("error", "loadMeetings: ${it.message}")
            }
        }
    }

    fun exitOffering() {
        _onExitOfferingEvent.setValue(Unit)
    }

    fun onBackClick() {
        _onBackPressedEvent.setValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    private fun stopPolling() {
        pollJob?.cancel()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            offeringRepository: OfferingRepository,
            participantRepository: ParticipantRepository,
            commentDetailRepository: CommentDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return com.zzang.chongdae.presentation.view.commentdetail.CommentDetailViewModel(
                    offeringId = offeringId,
                    offeringRepository = offeringRepository,
                    participantRepository = participantRepository,
                    commentDetailRepository = commentDetailRepository,
                ) as T
            }
        }
    }
}
