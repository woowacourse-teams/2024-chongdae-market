package com.zzang.chongdae.domain.usecase.home

import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.repository.FakeOfferingRepository
import com.zzang.chongdae.util.TestFixture.FILTERS_STUB
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchFiltersUserCaseTest {
    private lateinit var offeringRepository: OfferingRepository
    private lateinit var fetchFiltersUserCase: FetchFiltersUserCase

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        fetchFiltersUserCase = FetchFiltersUserCase(offeringRepository)
    }

    @Test
    fun `필터 정보를 가져온다`() =
        runTest {
            // when
            val actual = fetchFiltersUserCase().getOrThrow()

            // then
            assertThat(actual).isEqualTo(FILTERS_STUB)
        }
}
