package com.zzang.chongdae.presentation.view.home

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.zzang.chongdae.presentation.view.home.component.OfferingSearchBar
import com.zzang.chongdae.presentation.view.home.component.OfferingSearchState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class OfferingSearchBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `입력한_텍스트가_없을_경우_지정한_placeholder를_가진_검색바가_화면에_나온다`() {
        // given
        val state = OfferingSearchState(
            initialText = "",
            placeholder = "Test",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, {}, {})
        }

        // then
        composeTestRule.onNodeWithText("Test")
            .isDisplayed()
    }

    @Test
    fun `입력한_텍스트가_있을_경우_지정한_placeholder를_가진_검색바가_화면에_나오지_않는다`() {
        // given
        val state = OfferingSearchState(
            initialText = "1",
            placeholder = "Test",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, {}, {})
        }

        // then
        composeTestRule.onNodeWithText("Test")
            .isNotDisplayed()
    }

    @Test
    fun `입력한_텍스트가_없을_경우_x버튼이_나오지_않는다`() {
        // given
        val state = OfferingSearchState(
            initialText = "",
            placeholder = "",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, {}, {})
        }

        // then
        composeTestRule.onNodeWithContentDescription("Clear Button")
            .isNotDisplayed()
    }

    @Test
    fun `입력한_텍스트가_있을_경우_x버튼이_나온다`() {
        // given
        val state = OfferingSearchState(
            initialText = "1",
            placeholder = "",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, {}, {})
        }

        // then
        composeTestRule.onNodeWithContentDescription("Clear Button")
            .isDisplayed()
    }

    @Test
    fun `x버튼을_누를경우_onClearClick이_호출된다`() {
        // given
        var isClicked = false
        val state = OfferingSearchState(
            initialText = "1",
            placeholder = "",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, {}, { isClicked = true })
        }

        // when
        composeTestRule.onNodeWithContentDescription("Clear Button")
            .performClick()

        // then
        composeTestRule.waitForIdle()
        assertThat(isClicked).isTrue()
    }

    @Test
    fun `검색버튼을_누를경우_onSearchClick이_호출된다`() {
        // given
        var isClicked = false
        val state = OfferingSearchState(
            initialText = "",
            placeholder = "",
        )
        composeTestRule.setContent {
            OfferingSearchBar(state, { isClicked = true }, {})
        }

        // when
        composeTestRule.onNodeWithContentDescription("Search Button")
            .performClick()

        // then
        composeTestRule.waitForIdle()
        assertThat(isClicked).isTrue()
    }
}
