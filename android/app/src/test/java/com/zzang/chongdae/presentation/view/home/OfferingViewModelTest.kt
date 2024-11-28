package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.repository.FakeOfferingDetailRepository
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

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class OfferingViewModelTest {
    private lateinit var viewModel: OfferingViewModel
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var offeringDetailRepository: OfferingDetailRepository

    @BeforeEach
    fun setUp() {
        offeringRepository = mockk<OfferingRepository>()
        offeringDetailRepository = FakeOfferingDetailRepository()

        coEvery {
            offeringRepository.fetchOfferings(null, null, null, null)
        } returns Result.success(TestFixture.OFFERINGS_STUB)

        coEvery {
            offeringRepository.fetchFilters().getOrThrow()
        } returns TestFixture.FILTERS_STUB

        viewModel = OfferingViewModel(offeringRepository, offeringDetailRepository)
    }

    @DisplayName("필터 정보를 불러온다")
    @Test
    fun fetchFilters() {
        // given
        coEvery {
            offeringRepository.fetchFilters().getOrThrow()
        } returns TestFixture.FILTERS_STUB

        // when
        val actual = viewModel.filters.getOrAwaitValue()

        // then
        assertThat(actual).hasSize(4)
    }
}
