package com.zzang.chongdae.presentation.view.home


import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class OfferingViewModelTest {
    private lateinit var viewModel: OfferingViewModel
    private lateinit var offeringRepository: OfferingRepository

    @BeforeEach
    fun setUp() {
        offeringRepository = mockk<OfferingRepository>()

        coEvery {
            offeringRepository.fetchOfferings(null, null, null, null)
        } returns OFFERINGS_STUB

        coEvery {
            offeringRepository.fetchFilters().getOrThrow()
        } returns FILTERS_STUB

        viewModel = OfferingViewModel(offeringRepository)
    }

    @Test
    fun `필터 정보를 불러온다`() {
        // given
        coEvery {
            offeringRepository.fetchFilters().getOrThrow()
        } returns FILTERS_STUB

        // when
        val actual = viewModel.filters.getOrAwaitValue()

        // then
        assertThat(actual).hasSize(4)
    }

    companion object {
        private val OFFERINGS_STUB = (0..20).map {
            Offering(
                id = it.toLong(),
                title = "",
                meetingAddress = "",
                thumbnailUrl = null,
                totalCount = 0,
                currentCount = 0,
                dividedPrice = 0,
                eachPrice = null,
                condition = OfferingCondition.CONFIRMED,
                isOpen = false
            )
        }
        private val FILTERS_STUB = (0..3).map {
            Filter(
                name = FilterName.HIGH_DISCOUNT,
                value = "",
                type = FilterType.VISIBLE
            )
        }
    }
}
