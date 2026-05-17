package com.zzang.chongdae.domain.usecase.login

import com.zzang.chongdae.auth.data.repository.AuthRepository
import com.zzang.chongdae.data.local.datastore.UserPreferencesDataStore
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeDataStore
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PostLoginUseCaseTest {
    private lateinit var authRepository: com.zzang.chongdae.auth.data.repository.AuthRepository
    private lateinit var userPreferenceDataStore: UserPreferencesDataStore
    private lateinit var postLoginUseCase: PostLoginUseCase

    @BeforeEach
    fun setUp() {
        authRepository = FakeAuthRepository()
        userPreferenceDataStore = UserPreferencesDataStore(FakeDataStore())
        postLoginUseCase = PostLoginUseCaseImpl(authRepository, userPreferenceDataStore)
    }

    @Test
    fun `로그인에 성공한다`() =
        runTest {
            // given

            // when
            val result = postLoginUseCase("FakeAccessToken", "FakeRefreshToken")

            // then
            assertThat(result).isInstanceOf(com.zzang.chongdae.common.handler.Result.Success::class.java)
        }
}
