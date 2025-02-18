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

    @Test
    @DisplayName("댓글 상세 화면이 보여야 한다")
    fun displayCommentDetailTest() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    @DisplayName("공동구매 상태 이미지를 로드한다")
    fun loadOfferingStatusImage() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.iv_offering_status))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    @DisplayName("공동구매 약속 장소와 시간을 로드한다")
    fun loadOfferingLocation() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.tv_default_location))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    @DisplayName("댓글 목록을 로드한다")
    fun loadComments() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.rv_comments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    @DisplayName("댓글을 입력할 수 있는 EditText가 보여야 한다")
    fun displayCommentEditText() {
        // then
        Espresso.onView(ViewMatchers.withId(R.id.et_comment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
