package com.zzang.chongdae.domain.usecase.offeringdetail

import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.repository.FakeOfferingDetailRepository
import com.zzang.chongdae.util.TestFixture.OFFERING_DETAIL_STUB
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchOfferingDetailUseCaseTest {
    private lateinit var offeringDetailRepository: OfferingDetailRepository
    private lateinit var fetchOfferingDetailUseCase: FetchOfferingDetailUseCase

    @BeforeEach
    fun setUp() {
        offeringDetailRepository = FakeOfferingDetailRepository()
        fetchOfferingDetailUseCase = FetchOfferingDetailUseCase(offeringDetailRepository)
    }

    @Test
    fun `공모 상세 정보를 가져온다`() = runTest {
        // when
        val actual = fetchOfferingDetailUseCase(1L).getOrThrow()

        // then
        assertThat(actual).isEqualTo(OFFERING_DETAIL_STUB)
    }
}
