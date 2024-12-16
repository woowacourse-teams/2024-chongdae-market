package com.zzang.chongdae.domain.usecase.login

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeDataStore
import com.zzang.chongdae.util.CoroutinesTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class PostLoginUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userPreferenceDataStore: UserPreferencesDataStore
    private lateinit var postLoginUseCase: PostLoginUseCase

    @BeforeEach
    fun setUp() {
        authRepository = FakeAuthRepository()
        userPreferenceDataStore = UserPreferencesDataStore(FakeDataStore())
        postLoginUseCase = PostLoginUseCaseImpl(authRepository, userPreferenceDataStore)
    }

    @Test
    fun `로그인에 성공한다`() = runTest {
        // given

        // when
        val result = postLoginUseCase("FakeAccessToken", "FakeRefreshToken")

        // then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}