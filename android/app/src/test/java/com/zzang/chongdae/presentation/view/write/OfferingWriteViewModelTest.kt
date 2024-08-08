package com.zzang.chongdae.presentation.view.write

import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.presentation.view.write.OfferingWriteViewModel.Companion.HTTPS
import com.zzang.chongdae.repository.FakeOfferingRepository
import com.zzang.chongdae.util.CoroutinesTestExtension
import com.zzang.chongdae.util.InstantTaskExecutorExtension
import com.zzang.chongdae.util.TestFixture.martiPartBody
import com.zzang.chongdae.util.TestFixture.productUrl
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
class OfferingWriteViewModelTest {
    private lateinit var viewModel: OfferingWriteViewModel
    private lateinit var offeringRepository: OfferingRepository

    @BeforeEach
    fun setUp() {
        offeringRepository = FakeOfferingRepository()
        viewModel = OfferingWriteViewModel(offeringRepository)
    }

    @DisplayName("상품 url을 통해 og 이미지 정보를 가져온다")
    @Test
    fun saveProductImageOg() {
        // given
        // when
        viewModel.postProductImageOg()

        // then
        val result = viewModel.thumbnailUrl.getOrAwaitValue()
        assertThat(result).isEqualTo(HTTPS + productUrl.imageUrl)
    }

    @DisplayName("상품 이미지를 S3에 저장한다")
    @Test
    fun saveProductImageS3() {
        // given
        // when
        viewModel.uploadImageFile(martiPartBody)

        // then
        val result = viewModel.thumbnailUrl.getOrAwaitValue()
        assertThat(result).isEqualTo(productUrl.imageUrl)
    }

    @DisplayName("업로드한 상품 이미지를 지울 수 있다")
    @Test
    fun deleteProductImage() {
        // given
        viewModel.uploadImageFile(martiPartBody)

        // when
        viewModel.clearProductImage()

        // then
        val result = viewModel.thumbnailUrl.getOrAwaitValue()
        assertThat(result).isEqualTo(null)
    }
}
