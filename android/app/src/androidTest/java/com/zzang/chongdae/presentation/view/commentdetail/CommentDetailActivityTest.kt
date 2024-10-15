package com.zzang.chongdae.presentation.view.commentdetail

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zzang.chongdae.R
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentDetailActivityTest {
    private lateinit var scenario: ActivityScenario<CommentDetailActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(CommentDetailActivity::class.java)
    }

// 안드코드 수정
    @Test
    @DisplayName("댓글 상세 화면이 보여야 한다")
    fun displayCommentDetailTest() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
