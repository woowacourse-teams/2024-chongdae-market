package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.repository.FakeAuthRepository
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
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.repository.FakeOfferingRepository

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class OfferingViewModelTest {
    private lateinit var viewModel: OfferingViewModel
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        authRepository = FakeAuthRepository()
        viewModel = OfferingViewModel(offeringRepository, authRepository)
    }

    @DisplayName("필터 정보를 불러온다")
    @Test
    fun fetchFilters() {
        // given

        // when
        val actual = viewModel.filters.getOrAwaitValue()

        // then
        assertThat(actual).hasSize(4)
    }
}
