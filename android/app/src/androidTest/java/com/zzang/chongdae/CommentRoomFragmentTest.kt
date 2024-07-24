package com.zzang.chongdae

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.presentation.view.comment.CommentRoomFragment
import com.zzang.chongdae.presentation.view.comment.adapter.CommentRoomViewHolder
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CommentRoomFragmentTest {
    private lateinit var scenario: FragmentScenario<CommentRoomFragment>

    @Before
    fun setUp() {
        scenario = FragmentScenario.launchInContainer(CommentRoomFragment::class.java)
    }

    @Test
    @DisplayName("댓글방이 하나도 없다면 그것을 알려주는 뷰가 보여야 한다")
    fun `댓글방이_하나도_없다면_그것을_알려주는_뷰가_보여야_한다`() {
        // then
        onView(withId(R.id.iv_empty_comment_room)).check(matches(isDisplayed()))
    }

    @Test
    @DisplayName("댓글방이 있으면 댓글방의 뷰가 보여야 한다")
    fun `댓글방이_있으면_댓글방의_뷰가_보여야_한다`() {
        // given
        scenario.onFragment {
            it.viewModel.putCommentRooms(
                listOf(CommentRoom(1, "알송", "알송이에용", LocalDateTime.now(), true)),
            )
        }
        // when
        onView(withId(R.id.rv_comment_room)).perform(
            RecyclerViewActions.scrollToPosition<CommentRoomViewHolder>(0),
        )
        // then
        onView(withText("알송")).check(matches(isDisplayed()))
    }
}
