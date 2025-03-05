package com.zzang.chongdae.presentation.view.home

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.presentation.view.home.component.OfferingFilters
import com.zzang.chongdae.presentation.view.home.component.OfferingFiltersState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class OfferingFiltersTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `OfferingFilters의_인자로_들어간_필터들이_화면에_나온다`() {
        // given
        val filters = listOf(
            Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
            Filter(FilterName.IMMINENT, "마감임박만", FilterType.VISIBLE),
        )
        val state = OfferingFiltersState(filters) { _, _ -> }
        composeTestRule.setContent {
            OfferingFilters(state)
        }

        // then
        composeTestRule
            .onNodeWithText("참여가능만")
            .isDisplayed()

        composeTestRule
            .onNodeWithText("마감임박만")
            .isDisplayed()
    }

    @Test
    fun `FilterType이_INVISIBLE인_필터는_화면에_나오지_않는다`() {
        // given
        val filters = listOf(
            Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
            Filter(FilterName.IMMINENT, "마감임박만", FilterType.INVISIBLE),
        )
        val state = OfferingFiltersState(filters) { _, _ -> }
        composeTestRule.setContent {
            OfferingFilters(state)
        }

        // then
        composeTestRule
            .onNodeWithText("참여가능만")
            .isDisplayed()

        composeTestRule
            .onNodeWithText("마감임박만")
            .isNotDisplayed()
    }

    @Test
    fun `화면시작_시_아무_필터도_선택되어있지_않다`() {
        // given
        val filters = listOf(
            Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
            Filter(FilterName.IMMINENT, "마감임박만", FilterType.INVISIBLE),
        )
        val state = OfferingFiltersState(filters) { _, _ -> }
        composeTestRule.setContent {
            OfferingFilters(state)
        }


        // then
        composeTestRule.waitForIdle()
        assertThat(state.selectedFilter).isEqualTo(null)
    }

    @Test
    fun `필터를_누르면_해당_필터가_선택된다`() {
        // given
        val filters = listOf(
            Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
            Filter(FilterName.IMMINENT, "마감임박만", FilterType.INVISIBLE),
        )
        val state = OfferingFiltersState(filters) { _, _ -> }
        composeTestRule.setContent {
            OfferingFilters(state)
        }

        // when
        composeTestRule.onNodeWithText("참여가능만")
            .performClick()

        // then
        composeTestRule.waitForIdle()
        assertThat(state.selectedFilter?.value).isEqualTo("참여가능만")
    }

    @Test
    fun `선택된_필터를_누르면_해당_필터의_선택이_해제된다`() {
        // given
        val filters = listOf(
            Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
            Filter(FilterName.IMMINENT, "마감임박만", FilterType.INVISIBLE),
        )
        val state = OfferingFiltersState(filters) { _, _ -> }
        composeTestRule.setContent {
            OfferingFilters(state)
        }

        composeTestRule.onNodeWithText("참여가능만")
            .performClick()

        // when
        composeTestRule.onNodeWithText("참여가능만")
            .performClick()

        // then
        composeTestRule.waitForIdle()
        assertThat(state.selectedFilter?.value).isEqualTo(null)
    }
}
