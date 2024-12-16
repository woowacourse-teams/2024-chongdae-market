//package com.zzang.chongdae.domain.usecase.login
//
//import com.zzang.chongdae.auth.repository.AuthRepository
//import com.zzang.chongdae.common.datastore.UserPreferencesDataStore
//import com.zzang.chongdae.repository.FakeAuthRepository
//import com.zzang.chongdae.repository.FakeDataStore
//import com.zzang.chongdae.util.CoroutinesTestExtension
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//
//@ExperimentalCoroutinesApi
//@ExtendWith(CoroutinesTestExtension::class)
//class PostLoginUseCaseTest {
//    private lateinit var authRepository: AuthRepository
//    private lateinit var userPreferenceDataStore: UserPreferencesDataStore
//    private lateinit var postLoginUseCase: PostLoginUseCase
//
//    @BeforeEach
//    fun setUp() {
//        authRepository = FakeAuthRepository()
//        userPreferenceDataStore = UserPreferencesDataStore(FakeDataStore())
//        postLoginUseCase = PostLoginUseCaseImpl(authRepository, userPreferenceDataStore)
//    }
//
//    @Test
//    fun `임시 이름`() {
//        // given
//        val a = postLoginUseCase("FakeAccessToken", "FakeRefreshToken")
//
//        // when
//
//        // then
//
//    }
//}