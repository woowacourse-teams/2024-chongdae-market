package com.zzang.chongdae.presentation.view.commentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.R
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.comment.Comment
import com.zzang.chongdae.domain.usecase.analytics.FetchUserTypeUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.DeleteParticipationsUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.FetchCommentOfferingInfoUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.FetchCommentsUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.FetchMeetingsUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.FetchParticipantsUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.SaveCommentUseCase
import com.zzang.chongdae.domain.usecase.commentdetail.UpdateOfferingStatusUseCase
import com.zzang.chongdae.presentation.util.Event
import com.zzang.chongdae.presentation.view.commentdetail.event.CommentDetailEvent
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel.Companion.toUiModelListWithSeparators
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.information.CommentOfferingInfoUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.meeting.MeetingsUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantsUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.usertype.UserTypeUiModel
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
        private val fetchCommentsUseCase: FetchCommentsUseCase,
        private val saveCommentUseCase: SaveCommentUseCase,
        private val fetchCommentOfferingInfoUseCase: FetchCommentOfferingInfoUseCase,
        private val updateOfferingStatusUseCase: UpdateOfferingStatusUseCase,
        private val fetchParticipantsUseCase: FetchParticipantsUseCase,
        private val deleteParticipationsUseCase: DeleteParticipationsUseCase,
        private val fetchMeetingsUseCase: FetchMeetingsUseCase,
        private val fetchUserTypeUseCase: FetchUserTypeUseCase,
    ) : ViewModel(), OnAlertClickListener {
        @AssistedFactory
        interface CommentDetailAssistedFactory {
            fun create(offeringId: Long): CommentDetailViewModel
        }

        private var pollJob: Job? = null

        private val _isPollingActive = MutableLiveData(false)
        val isPollingActive: LiveData<Boolean> get() = _isPollingActive

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

        private val _exitLoading: MutableLiveData<Boolean> = MutableLiveData(false)
        val exitLoading: LiveData<Boolean> get() = _exitLoading

        private val _userType = MutableLiveData<UserTypeUiModel>()
        val userType: LiveData<UserTypeUiModel> get() = _userType

        init {
            fetchUserType()
            startPolling()
            updateCommentInfo()
            loadMeetings()
            loadParticipants()
        }

        private fun fetchUserType() {
            viewModelScope.launch {
                when (val result = fetchUserTypeUseCase()) {
                    is Result.Success -> _userType.value = UserTypeUiModel.from(result.data)
                    is Result.Error -> _userType.value = UserTypeUiModel.A
                }
            }
        }

        private fun startPolling() {
            stopPolling()
            _isPollingActive.value = true
            pollJob =
                viewModelScope.launch {
                    while (this.isActive) {
                        loadComments()
                        delay(1000)
                    }
                    _isPollingActive.value = false
                }
        }

        private fun stopPolling() {
            pollJob?.cancel()
            _isPollingActive.value = false
        }

        private fun handleNetworkError(
            error: DataError.Network,
            retryAction: suspend () -> Unit = {},
        ) {
            val message =
                when (error) {
                    DataError.Network.UNAUTHORIZED -> "로그아웃 후 다시 진행해주세요."
                    DataError.Network.CONNECTION_ERROR -> "네트워크 연결을 확인해주세요."
                    else -> error.name
                }
            _event.value = Event(CommentDetailEvent.ShowError(message))
            stopPolling()
        }

        private fun updateCommentInfo() {
            viewModelScope.launch {
                when (val result = fetchCommentOfferingInfoUseCase(offeringId)) {
                    is Result.Success -> _commentOfferingInfo.value = result.data.toUiModel()
                    is Result.Error -> {
                        handleNetworkError(result.error) { updateCommentInfo() }
                    }
                }
            }
        }

        fun updateOfferingEvent() {
            _event.value = Event(CommentDetailEvent.ShowUpdateStatusDialog)
        }

        fun updateOfferingStatus() {
            viewModelScope.launch {
                when (val result = updateOfferingStatusUseCase(offeringId)) {
                    is Result.Success -> updateCommentInfo()
                    is Result.Error -> handleNetworkError(result.error) { updateOfferingStatus() }
                }
            }
        }

        fun loadComments() {
            viewModelScope.launch {
                when (val result = fetchCommentsUseCase(offeringId)) {
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

            val type = userType.value ?: UserTypeUiModel.A
            val logEventId =
                when (type) {
                    UserTypeUiModel.A -> "post_comment_a"
                    UserTypeUiModel.B -> "post_comment_b"
                }

            _event.value = Event(CommentDetailEvent.LogAnalytics(logEventId))

            viewModelScope.launch {
                when (val result = saveCommentUseCase(offeringId, content)) {
                    is Result.Success -> commentContent.value = ""
                    is Result.Error -> handleNetworkError(result.error) { postComment() }
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
                when (val result = fetchParticipantsUseCase(offeringId)) {
                    is Result.Success -> _participants.value = result.data.toUiModel()
                    is Result.Error -> handleNetworkError(result.error) { loadParticipants() }
                }
            }
        }

        private fun loadMeetings() {
            viewModelScope.launch {
                when (val result = fetchMeetingsUseCase(offeringId)) {
                    is Result.Success -> _meetings.value = result.data.toUiModel()
                    is Result.Error -> handleNetworkError(result.error) { loadMeetings() }
                }
            }
        }

        fun onClickReport() {
            _event.value = Event(CommentDetailEvent.ShowReport(R.string.report_url))
        }

        private fun exitOffering() {
            _exitLoading.value = true
            viewModelScope.launch {
                val isProposer = _commentOfferingInfo.value?.isProposer ?: false
                if (isProposer) {
                    _event.value = Event(CommentDetailEvent.ShowError("총대는 채팅방을 나갈 수 없습니다."))
                    _exitLoading.value = false
                    return@launch
                }

                when (val result = deleteParticipationsUseCase(offeringId)) {
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

                            else -> {
                                handleNetworkError(result.error) { exitOffering() }
                            }
                        }
                }
                _exitLoading.value = false
            }
        }

        fun onMoreOptionsClick() {
            val type = userType.value ?: UserTypeUiModel.A
            val id =
                when (type) {
                    UserTypeUiModel.A -> "more_comment_detail_options_event_a"
                    UserTypeUiModel.B -> "more_comment_detail_options_event_b"
                }
            _event.value = Event(CommentDetailEvent.LogAnalytics(id))
            _event.value = Event(CommentDetailEvent.OpenDrawer)
        }

        fun onBackClick() {
            _event.value = Event(CommentDetailEvent.BackPressed)
        }

        fun onExitClick() {
            _event.value = Event(CommentDetailEvent.ShowAlert)
        }

        override fun onClickConfirm() {
            _event.value = Event(CommentDetailEvent.AlertCancelled)
            exitOffering()
        }

        override fun onClickCancel() {
            _event.value = Event(CommentDetailEvent.AlertCancelled)
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
