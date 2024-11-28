package com.zzang.chongdae.presentation.view.offeringdetail

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeDataStore
import com.zzang.chongdae.repository.FakeOfferingDetailRepository
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
class OfferingDetailViewModelTest {
    private lateinit var viewModel: OfferingDetailViewModel
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var offeringDetailRepository: OfferingDetailRepository
    private lateinit var authRepository: AuthRepository
    private val offeringId = 1L

    @BeforeEach
    fun setUp() {
        dataStore = FakeDataStore()
        offeringDetailRepository = FakeOfferingDetailRepository()
        authRepository = FakeAuthRepository()

        viewModel = OfferingDetailViewModel(offeringId, offeringDetailRepository, authRepository)
    }

    @DisplayName("공구에 참여한다")
    @Test
    fun onClickParticipation() {
        // given

        // when
        viewModel.participate()
        val actual = viewModel.isParticipated.getOrAwaitValue()

        // then
        assertThat(actual).isTrue()
    }
}
