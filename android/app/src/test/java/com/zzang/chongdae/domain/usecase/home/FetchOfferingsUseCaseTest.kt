package com.zzang.chongdae.domain.usecase.home

import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.repository.FakeOfferingRepository
import com.zzang.chongdae.util.TestFixture.OFFERINGS_STUB
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchOfferingsUseCaseTest {
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var fetchOfferingsUseCase: FetchOfferingsUseCase

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        fetchOfferingsUseCase = FetchOfferingsUseCase(offeringRepository)
    }

    @Test
    fun `공모들을 불러온다`() =
        runTest {
            // when
            val actual = fetchOfferingsUseCase(null, null, null, null).getOrThrow()

            // then
            assertThat(actual).isEqualTo(OFFERINGS_STUB)
        }
}
