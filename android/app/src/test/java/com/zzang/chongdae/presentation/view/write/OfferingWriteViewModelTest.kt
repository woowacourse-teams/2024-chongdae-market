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

    @DisplayName("총원과 총 가격을 입력받았을 때 엔빵 가격을 계산할 수 있어야 한다")
    @Test
    fun calculateSplitPrice() {
        // when
        viewModel.totalCount.value = "3"
        viewModel.totalPrice.value = "3000"

        // then
        val result = viewModel.splitPrice.getOrAwaitValue()
        assertThat(result).isEqualTo(1000)
    }

    @DisplayName("총원과 총 가격, 낱개 가격을 입력받았을 때 할인율을 계산할 수 있어야 한다")
    @Test
    fun calculateDiscountRate() {
        // when
        viewModel.totalCount.value = "3"
        viewModel.totalPrice.value = "3000"
        viewModel.originPrice.value = "2000"

        // then
        val result = viewModel.discountRate.getOrAwaitValue()
        assertThat(result).isEqualTo(50f)
    }

    @DisplayName("필수 항목이 모두 입력되어야만 제출 버튼이 활성화 되어야 한다")
    @Test
    fun enabledSubmitButton() {
        // when
        viewModel.apply {
            title.value = "테스트 제목"
            totalCount.value = "2"
            totalPrice.value = "1000"
            meetingAddress.value = "테스트 장소"
            tradeDate.value = "테스트 시간"
        }

        // then
        val result = viewModel.essentialSubmitButtonEnabled.getOrAwaitValue()
        assertThat(result).isTrue()
    }

    @DisplayName("필수 항목이 모두 입력되지 않으면 제출 버튼이 비활성화 되어야 한다")
    @Test
    fun disabledSubmitButton() {
        // when
        viewModel.apply {
            title.value = "테스트 제목"
            totalCount.value = "\n"
            totalPrice.value = ""
            meetingAddress.value = "  "
            tradeDate.value = "테스트 시간"
        }

        // then
        val result = viewModel.essentialSubmitButtonEnabled.getOrAwaitValue()
        assertThat(result).isFalse()
    }
}
