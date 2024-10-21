package com.zzang.chongdae.presentation.view.commentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.R
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.CommentDetailRepositoryQualifier
import com.zzang.chongdae.di.annotations.OfferingRepositoryQualifier
import com.zzang.chongdae.di.annotations.ParticipantRepositoryQualifier
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.presentation.util.Event
import com.zzang.chongdae.presentation.view.commentdetail.event.CommentDetailEvent
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel.Companion.toUiModelListWithSeparators
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.common.OnAlertClickListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CommentDetailViewModel
    @AssistedInject
    constructor(
        @Assisted private val offeringId: Long,
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        @OfferingRepositoryQualifier private val offeringRepository: OfferingRepository,
        @ParticipantRepositoryQualifier private val participantRepository: ParticipantRepository,
        @CommentDetailRepositoryQualifier private val commentDetailRepository: CommentDetailRepository,
    ) : ViewModel(),
        OnAlertClickListener {
        @AssistedFactory
        interface CommentDetailAssistedFactory {
            fun create(offeringId: Long): CommentDetailViewModel
        }

        private var pollJob: Job? = null

        val commentContent = MutableLiveData("")

        private val _comments: MutableLiveData<List<CommentUiModel>> = MutableLiveData()
        val comments: LiveData<List<CommentUiModel>> get() = _comments
        private var cachedComments: List<Comment> = emptyList()

        private val _commentOfferingInfo = MutableLiveData<CommentOfferingInfoUiModel>()
        val commentOfferingInfo: LiveData<CommentOfferingInfoUiModel> get() = _commentOfferingInfo

        private val _meetings = MutableLiveData<MeetingsUiModel>()
        val meetings: LiveData<MeetingsUiModel> get() = _meetings

        private val _participants = MutableLiveData<ParticipantsUiModel>()
        val participants: LiveData<ParticipantsUiModel> get() = _participants

        private val _isCollapsibleViewVisible = MutableLiveData(false)
        val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible

        private val _event = MutableLiveData<Event<CommentDetailEvent>>()
        val event: LiveData<Event<CommentDetailEvent>> get() = _event

        init {
            startPolling()
            updateCommentInfo()
            loadMeetings()
            loadParticipants()
        }

        private fun startPolling() {
            stopPolling()
            pollJob =
                viewModelScope.launch {
                    while (this.isActive) {
                        loadComments()
                        delay(1000)
                    }
                }
        }

        private fun handleNetworkError(
            error: DataError.Network,
            retryAction: suspend () -> Unit,
        ) {
            when (error) {
                DataError.Network.UNAUTHORIZED -> {
                    viewModelScope.launch {
                        when (authRepository.saveRefresh()) {
                            is Result.Success -> retryAction()
                            is Result.Error ->
                                _event.value =
                                    Event(CommentDetailEvent.ShowError("로그아웃 후 다시 진행해주세요."))
                        }
                    }
                }

                else -> _event.value = Event(CommentDetailEvent.ShowError(error.name))
            }
        }

        private fun updateCommentInfo() {
            viewModelScope.launch {
                when (val result = commentDetailRepository.fetchCommentOfferingInfo(offeringId)) {
                    is Result.Success -> _commentOfferingInfo.value = result.data.toUiModel()
                    is Result.Error ->
                        handleNetworkError(result.error) {
                            updateCommentInfo()
                        }
                }
            }
        }

        fun updateOfferingEvent() {
            _event.value = Event(CommentDetailEvent.ShowUpdateStatusDialog)
        }

        fun updateOfferingStatus() {
            viewModelScope.launch {
                when (val result = commentDetailRepository.updateOfferingStatus(offeringId)) {
                    is Result.Success -> updateCommentInfo()
                    is Result.Error ->
                        handleNetworkError(result.error) {
                            updateOfferingStatus()
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
                            _comments.value = newComments.toUiModelListWithSeparators()
                            cachedComments = newComments
                        }
                    }
                    is Result.Error -> handleNetworkError(result.error) { loadComments() }
                }
            }
        }

        fun postComment() {
            val content = commentContent.value?.trim()
            if (content.isNullOrEmpty()) return

            viewModelScope.launch {
                when (val result = commentDetailRepository.saveComment(offeringId, content)) {
                    is Result.Success -> commentContent.value = ""
                    is Result.Error ->
                        handleNetworkError(result.error) {
                            postComment()
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
                        handleNetworkError(result.error) {
                            loadParticipants()
                        }
                }
            }
        }

        private fun loadMeetings() {
            viewModelScope.launch {
                when (val result = offeringRepository.fetchMeetings(offeringId)) {
                    is Result.Success -> _meetings.value = result.data.toUiModel()
                    is Result.Error ->
                        handleNetworkError(result.error) {
                            loadMeetings()
                        }
                }
            }
        }

        fun onClickReport() {
            _event.value = Event(CommentDetailEvent.ShowReport(R.string.report_url))
        }

        private fun exitOffering() {
            viewModelScope.launch {
                when (val result = participantRepository.deleteParticipations(offeringId)) {
                    is Result.Success -> {
                        _event.value = Event(CommentDetailEvent.ExitOffering)
                        stopPolling()
                    }

                    is Result.Error ->
                        when (result.error) {
                            DataError.Network.NULL -> {
                                _event.value = Event(CommentDetailEvent.ExitOffering)
                                stopPolling()
                            }

                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> exitOffering()
                                    is Result.Error ->
                                        _event.value =
                                            Event(CommentDetailEvent.ShowError("로그아웃 후 다시 진행해주세요."))
                                }
                            }

                            else -> {
                                return@launch
                            }
                        }
                }
            }
        }

        fun onBackClick() {
            _event.value = Event(CommentDetailEvent.BackPressed)
        }

        fun onExitClick() {
            _event.value = Event(CommentDetailEvent.ShowAlert)
        }

        override fun onClickConfirm() {
            exitOffering()
        }

        override fun onClickCancel() {
            _event.value = Event(CommentDetailEvent.AlertCancelled)
        }

        private fun stopPolling() {
            pollJob?.cancel()
        }

        override fun onCleared() {
            super.onCleared()
            stopPolling()
        }

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun getFactory(
                assistedFactory: CommentDetailAssistedFactory,
                offeringId: Long,
            ) = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(offeringId) as T
                }
            }
        }
    }
