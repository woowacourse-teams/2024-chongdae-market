package com.zzang.chongdae.presentation.view.comment

import com.zzang.chongdae.domain.usecase.comment.UpdateCommentRoomsUseCaseImpl
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeCommentRoomsRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CommentRoomsViewModelTest {
    private lateinit var viewModel: CommentRoomsViewModel

    @BeforeEach
    fun setUp() {
        val fakeAuthRepository = FakeAuthRepository()
        val fakeCommentRoomsRepository = FakeCommentRoomsRepository()

        val updateCommentRoomsUseCase = UpdateCommentRoomsUseCaseImpl(fakeAuthRepository, fakeCommentRoomsRepository)

        viewModel = CommentRoomsViewModel(updateCommentRoomsUseCase)
    }

    @DisplayName("댓글방 목록을 확인할 수 있어야 한다")
    @Test
    fun loadCommentRooms() {
        // when
        viewModel.updateCommentRooms()

        // then
        val actual = viewModel.commentRooms.getOrAwaitValue()
        assertThat(actual[0].id).isEqualTo(1)
        assertThat(actual[0].title).isEqualTo("title")
        assertThat(actual[0].latestComment).isEqualTo("latestComment")
        assertThat(actual[0].latestCommentTime).isEqualTo(LocalDateTime.of(1, 1, 1, 0, 0))
        assertThat(actual[0].isProposer).isEqualTo(true)
    }
}
