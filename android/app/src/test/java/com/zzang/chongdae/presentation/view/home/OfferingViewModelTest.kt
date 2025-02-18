package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.usecase.home.FetchFiltersUserCase
import com.zzang.chongdae.domain.usecase.home.FetchOfferingUseCase
import com.zzang.chongdae.domain.usecase.home.FetchOfferingsUseCase
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeDataStore
import com.zzang.chongdae.repository.FakeOfferingRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.getOrAwaitValue
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
    private lateinit var authRepository: AuthRepository
    private lateinit var fetchOfferingsUseCase: FetchOfferingsUseCase
    private lateinit var fetchFiltersUserCase: FetchFiltersUserCase
    private lateinit var fetchOfferingUseCase: FetchOfferingUseCase
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        authRepository = FakeAuthRepository()
        fetchOfferingsUseCase = FetchOfferingsUseCase(offeringRepository)
        fetchFiltersUserCase = FetchFiltersUserCase(offeringRepository)
        fetchOfferingUseCase = FetchOfferingUseCase(offeringRepository)
        userPreferencesDataStore = UserPreferencesDataStore(FakeDataStore())
        viewModel =
            OfferingViewModel(
                authRepository,
                fetchOfferingsUseCase,
                fetchFiltersUserCase,
                fetchOfferingUseCase,
                userPreferencesDataStore,
            )
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
