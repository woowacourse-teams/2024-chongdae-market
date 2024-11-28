package com.zzang.chongdae

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zzang.chongdae.presentation.view.comment.CommentRoomFragment
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentRoomFragmentTest {
    private lateinit var scenario: FragmentScenario<CommentRoomFragment>

    @Before
    fun setUp() {
        scenario = FragmentScenario.launchInContainer(CommentRoomFragment::class.java)
    }

    @Test
    @DisplayName("댓글방 목록으로 이동하면 채팅이라는 텍스트뷰가 보여야 한다")
    fun commentRoomTest1() {
        // then
        onView(withId(R.id.tv_comment_text)).check(matches(isDisplayed()))
    }
}
