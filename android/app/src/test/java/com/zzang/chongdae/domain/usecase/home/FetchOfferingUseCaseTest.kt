package com.zzang.chongdae.domain.usecase.home

import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.repository.FakeOfferingRepository
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchOfferingUseCaseTest {
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var fetchOfferingUseCase: FetchOfferingUseCase

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        fetchOfferingUseCase = FetchOfferingUseCase(offeringRepository)
    }

    @Test
    fun `offering ID에 해당하는 공모를 가져온다`() =
        runTest {
            // when
            val actual = fetchOfferingUseCase(2L).getOrThrow()

            // then
            assertThat(actual.id).isEqualTo(2L)
        }
}
