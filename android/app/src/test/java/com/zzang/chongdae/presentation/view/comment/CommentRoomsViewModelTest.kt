package com.zzang.chongdae.presentation.view.comment

import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.TestFixture
import com.zzang.chongdae.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
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
    private lateinit var offeringRepository: CommentRoomsRepository

    @BeforeEach
    fun setUp() {
        offeringRepository = mockk<CommentRoomsRepository>()
        viewModel = CommentRoomsViewModel(offeringRepository)
    }

    @DisplayName("댓글방 목록을 확인할 수 있어야 한다")
    @Test
    fun loadCommentRooms() {
        // given
        coEvery {
            offeringRepository.fetchCommentRooms(1).getOrThrow()
        } returns TestFixture.COMMENT_ROOMS_STUB

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
