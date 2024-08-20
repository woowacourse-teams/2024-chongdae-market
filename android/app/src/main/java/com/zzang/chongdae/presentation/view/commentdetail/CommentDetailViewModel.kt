package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.presentation.util.MutableSingleLiveData
import com.zzang.chongdae.presentation.util.SingleLiveData
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel.Companion.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content

class CommentDetailViewModel(
    private val offeringId: Long,
    private val authRepository: AuthRepository,
    private val offeringRepository: OfferingRepository,
    private val participantRepository: ParticipantRepository,
    private val commentDetailRepository: CommentDetailRepository,
) : ViewModel() {
    private var cachedComments: List<Comment> = emptyList()
    private var pollJob: Job? = null
    val commentContent = MutableLiveData("")

    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _commentOfferingInfo = MutableLiveData<CommentOfferingInfoUiModel>()
    val commentOfferingInfo: LiveData<CommentOfferingInfoUiModel> get() = _commentOfferingInfo

    private val _meetings = MutableLiveData<MeetingsUiModel>()
    val meetings: LiveData<MeetingsUiModel> get() = _meetings

    private val _isCollapsibleViewVisible = MutableLiveData(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

    private val _participants = MutableLiveData<ParticipantsUiModel>()
    val participants: LiveData<ParticipantsUiModel> get() = _participants

    private val _showStatusDialogEvent = MutableLiveData<Unit>()
    val showStatusDialogEvent: LiveData<Unit> get() = _showStatusDialogEvent

    private val _reportEvent: MutableSingleLiveData<Int> = MutableSingleLiveData()
    val reportEvent: SingleLiveData<Int> get() = _reportEvent

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
            when (val result = commentDetailRepository.fetchCommentOfferingInfo(offeringId)) {
                is Result.Success -> _commentOfferingInfo.value = result.data.toUiModel()
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            updateCommentInfo()
                        }

                        else -> {
                            Log.e("error", "updateCommentInfo: ${result.error}")
                        }
                    }
            }
        }
    }

    fun updateOfferingEvent() {
        _showStatusDialogEvent.value = Unit
    }

    fun updateOfferingStatus() {
        viewModelScope.launch {
            when (val result = commentDetailRepository.updateOfferingStatus(offeringId)) {
                is Result.Success -> updateCommentInfo()
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            updateOfferingStatus()
                        }

                        else -> {
                            Log.e("error", "updateOfferingStatus: ${result.error}")
                        }
                    }
            }
        }
    }

    fun loadComments() {
        viewModelScope.launch {
            when (val result = commentDetailRepository.fetchComments(offeringId)) {
                is Result.Success -> {
                    val newComments = result.data
                    if (cachedComments != newComments) {
                        _comments.value = newComments
                        cachedComments = newComments
                    }
                }

                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            Log.d("error", "loadComments: ${result.error}")
                            authRepository.saveRefresh()
                            loadComments()
                        }

                        else -> {
                            Log.e("error", "loadComments: ${result.error}")
                        }
                    }
            }
        }
    }

    fun postComment() {
        val content = commentContent.value?.trim()
        if (content.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch {
            when (val result = commentDetailRepository.saveComment(offeringId, content)) {
                is Result.Success -> {
                    commentContent.value = ""
                }

                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            postComment()
                        }

                        else -> {
                            Log.e("error", "postComment: ${result.error}")
                        }
                    }
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
            when (val result = participantRepository.fetchParticipants(offeringId)) {
                is Result.Success -> _participants.value = result.data.toUiModel()
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            loadParticipants()
                        }

                        else -> {
                            Log.e("error", "loadParticipants: ${result.error}")
                        }
                    }
            }
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            when (val result = offeringRepository.fetchMeetings(offeringId)) {
                is Result.Success -> _meetings.value = result.data.toUiModel()
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            loadMeetings()
                        }

                        else -> {
                            Log.e("error", "loadMeetings: ${result.error}")
                        }
                    }
            }
        }
    }

    fun onClickReport() {
        _reportEvent.setValue(R.string.report_url)
    }

    fun exitOffering() {
        viewModelScope.launch {
            when (val result = participantRepository.deleteParticipations(offeringId)) {
                is Result.Success -> _onExitOfferingEvent.setValue(Unit)
                is Result.Error ->
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            exitOffering()
                        }

                        else -> {
                            Log.e("error", "exitOffering: ${result.error}")
                        }
                    }
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

    private fun stopPolling() {
        pollJob?.cancel()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            offeringId: Long,
            authRepository: AuthRepository,
            offeringRepository: OfferingRepository,
            participantRepository: ParticipantRepository,
            commentDetailRepository: CommentDetailRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return CommentDetailViewModel(
                    offeringId = offeringId,
                    authRepository = authRepository,
                    offeringRepository = offeringRepository,
                    participantRepository = participantRepository,
                    commentDetailRepository = commentDetailRepository,
                ) as T
            }
        }
    }
}
