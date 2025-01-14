package com.zzang.chongdae.presentation.view.commentdetail

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.presentation.view.commentdetail.event.CommentDetailEvent
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeCommentDetailRepository
import com.zzang.chongdae.repository.FakeOfferingRepository
import com.zzang.chongdae.repository.FakeParticipantRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.TestFixture
import com.zzang.chongdae.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CommentDetailViewModelTest {
    private lateinit var viewModel: CommentDetailViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var participantRepository: ParticipantRepository
    private lateinit var commentDetailRepository: CommentDetailRepository
    private val offeringId: Long = 1L

    @BeforeEach
    fun setUp() {
        authRepository = FakeAuthRepository()
        authRepository = FakeAuthRepository()
        offeringRepository = FakeOfferingRepository()
        participantRepository = FakeParticipantRepository()
        commentDetailRepository = FakeCommentDetailRepository()
        viewModel =
            CommentDetailViewModel(
                offeringId,
                authRepository,
                offeringRepository,
                participantRepository,
                commentDetailRepository,
            )
    }

    @DisplayName("공동구매 상태를 불러온다")
    @Test
    fun fetchOfferingStatus() {
        // given
        // when
        // then
        val result = viewModel.commentOfferingInfo.getOrAwaitValue()
        assertThat(result.imageUrl).isEqualTo(TestFixture.commentOfferingInfo.imageUrl)
    }

    @DisplayName("공동구매 상세 정보를 업데이트한다")
    @Test
    fun updateOffering() {
        // given
        // when
        viewModel.updateOfferingStatus()
        // then
        val result = viewModel.commentOfferingInfo.getOrAwaitValue()
        assertThat(result.imageUrl).isEqualTo(TestFixture.commentOfferingInfo2.imageUrl)
    }

    @DisplayName("댓글 목록을 불러온다")
    @Test
    fun fetchComments() {
        // given
        // when
        // then
        val result = viewModel.comments.getOrAwaitValue()
        assertThat(result).isEqualTo(TestFixture.commentsUiModels)
    }

    @DisplayName("댓글을 작성하면 댓글 목록에 추가된다")
    @Test
    fun addComment() {
        // given
        val before = viewModel.comments.getOrAwaitValue()

        // when
        viewModel.commentContent.value = "new comment"
        viewModel.postComment()
        viewModel.loadComments()

        // then
        val result = viewModel.comments.getOrAwaitValue()
        assertThat(result.size).isEqualTo(before.size + 1)
    }

    @DisplayName("약속 장소(도로명주소)를 불러온다")
    @Test
    fun fetchOfferingAddress() {
        // given
        // when
        // then
        val result = viewModel.meetings.getOrAwaitValue()
        assertThat(result.meetingAddress).isEqualTo(TestFixture.meetings.meetingAddress)
    }

    @DisplayName("약속 장소(상세주소)를 불러온다")
    @Test
    fun fetchOfferingAddressDetail() {
        // given
        // when
        // then
        val result = viewModel.meetings.getOrAwaitValue()
        assertThat(result.meetingAddressDetail).isEqualTo(TestFixture.meetings.meetingAddressDetail)
    }

    @DisplayName("약속 시간을 불러온다")
    @Test
    fun fetchOfferingDeadlineDetail() {
        // given
        // when
        // then
        val result = viewModel.meetings.getOrAwaitValue()
        assertThat(result.meetingDate).isEqualTo(TestFixture.meetings.meetingDate)
    }

    @DisplayName("CollapsibleView의 상태를 토글한다")
    @Test
    fun toggleCollapsibleViewChangesState() {
        // given
        val initialState = viewModel.isCollapsibleViewVisible.getOrAwaitValue()

        // when
        viewModel.toggleCollapsibleView()

        // then
        val updatedState = viewModel.isCollapsibleViewVisible.getOrAwaitValue()
        assertThat(updatedState).isNotEqualTo(initialState)
    }

    @DisplayName("Exit 버튼 클릭 시 이벤트를 발생시킨다")
    @Test
    fun triggerExitEvent() {
        // given
        // when
        viewModel.onExitClick()

        // then
        val event = viewModel.event.getOrAwaitValue()
        assertThat(event.peekContent()).isInstanceOf(CommentDetailEvent.ShowAlert::class.java)
    }

    @DisplayName("Polling 작업이 정상적으로 시작된다")
    @Test
    fun pollingStartsAndStops() {
        // given
        // when
        val isPolling = viewModel.isPollingActive.getOrAwaitValue()
        assertThat(isPolling).isEqualTo(true)
    }

    @DisplayName("Exit 확인 클릭 시 Polling 작업이 정상적으로 종료된다")
    @Test
    fun exitOfferingSuccessfully() {
        // given
        // when
        viewModel.onClickConfirm()

        // then
        val isPolling = viewModel.isPollingActive.getOrAwaitValue()
        assertThat(isPolling).isEqualTo(false)
    }
}
