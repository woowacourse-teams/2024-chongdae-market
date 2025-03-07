package com.zzang.chongdae.presentation.view.home

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.presentation.view.home.component.OfferingItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class OfferingItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `OfferingItem의_인자로_들어간_Offering이_화면에_나온다`() {
        // given
        composeTestRule.setContent {
            OfferingItem(OFFERING, onOfferingClick)
        }

        // then
        composeTestRule.onNodeWithText("Test")
            .isDisplayed()
    }

    @Test
    fun `OfferingItem을_클릭하면_onOfferingClick이_실행된다`() {
        // given
        var isClicked = false
        composeTestRule.setContent {
            OfferingItem(OFFERING, object : OnOfferingClickListener {
                override fun onClick(offeringId: Long) {
                    isClicked = true
                }
            })
        }

        // when
        composeTestRule.onNodeWithText("Test")
            .performClick()

        // then
        composeTestRule.waitForIdle()
        assertThat(isClicked).isTrue()
    }

    @Test
    fun `Offering_상태에_따라_다른_상태뱃지가_보인다_FULL`() {
        // given
        composeTestRule.setContent {
            OfferingItem(OFFERING.copy(status = OfferingCondition.FULL), onOfferingClick)
        }

        // then
        composeTestRule.onNodeWithText("인원만석")
            .isDisplayed()
    }

    @Test
    fun `Offering_상태에_따라_다른_상태뱃지가_보인다_IMMINENT`() {
        // given
        composeTestRule.setContent {
            OfferingItem(OFFERING.copy(status = OfferingCondition.IMMINENT), onOfferingClick)
        }

        // then
        composeTestRule.onNodeWithText("마감임박")
            .isDisplayed()
    }

    @Test
    fun `Offering_상태에_따라_다른_상태뱃지가_보인다_CONFIRMED`() {
        // given
        composeTestRule.setContent {
            OfferingItem(OFFERING.copy(status = OfferingCondition.CONFIRMED), onOfferingClick)
        }

        // then
        composeTestRule.onNodeWithText("모집마감")
            .isDisplayed()
    }

    @Test
    fun `Offering_상태에_따라_다른_상태뱃지가_보인다_AVAILABLE`() {
        // given
        composeTestRule.setContent {
            OfferingItem(OFFERING.copy(status = OfferingCondition.AVAILABLE), onOfferingClick)
        }

        // then
        composeTestRule.onNodeWithText("모집중")
            .isDisplayed()
    }

    companion object {
        private val OFFERING = Offering(
            id = 1,
            title = "Test",
            meetingAddressDong = "Test동",
            thumbnailUrl = "",
            totalCount = 3,
            currentCount = 2,
            dividedPrice = 2030,
            originPrice = 6090,
            status = OfferingCondition.IMMINENT,
            isOpen = false,
        )

        private val onOfferingClick = object : OnOfferingClickListener {
            override fun onClick(offeringId: Long) {
            }
        }
    }
}
