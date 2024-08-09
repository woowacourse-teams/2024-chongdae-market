package com.zzang.chongdae.presentation.view.offeringdetail

import android.content.Context
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.repository.FakeOfferingDetailRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.getOrAwaitValue
import io.mockk.every
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
class OfferingDetailViewModelTest {
    private lateinit var viewModel: OfferingDetailViewModel
    private lateinit var offeringDetailRepository: OfferingDetailRepository
    private lateinit var context: Context
    private val offeringId = 1L

    @BeforeEach
    fun setUp() {
        context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        offeringDetailRepository = FakeOfferingDetailRepository()
        viewModel = OfferingDetailViewModel(offeringId, offeringDetailRepository, context)
    }

    @DisplayName("공구에 참여한다")
    @Test
    fun onClickParticipation() {
        // given

        // when
        viewModel.onClickParticipation()
        val actual = viewModel.isParticipated.getOrAwaitValue()

        // then
        assertThat(actual).isTrue()
    }
}
