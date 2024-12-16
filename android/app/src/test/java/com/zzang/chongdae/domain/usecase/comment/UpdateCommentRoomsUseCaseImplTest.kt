package com.zzang.chongdae.domain.usecase.comment

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.repository.FakeAuthRepository
import com.zzang.chongdae.repository.FakeCommentRoomsRepository
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateCommentRoomsUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var commentRoomsRepository: CommentRoomsRepository
    private lateinit var updateCommentRoomsUseCase: UpdateCommentRoomsUseCase

    @BeforeEach
    fun setUp() {
        authRepository = FakeAuthRepository()
        commentRoomsRepository = FakeCommentRoomsRepository()
        updateCommentRoomsUseCase = UpdateCommentRoomsUseCaseImpl(authRepository, commentRoomsRepository)
    }

    @Test
    fun `댓글방 목록을 불러오는 데 성공한다`() = runTest {
        // given
        (commentRoomsRepository as FakeCommentRoomsRepository).isAccessTokenValid = true

        // when
        val result = updateCommentRoomsUseCase()

        // then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `AccessToken이 만료되면 토큰을 갱신하고 재시도해 댓글방 목록을 불러온다`() = runTest {
        // given
        (commentRoomsRepository as FakeCommentRoomsRepository).isAccessTokenValid = false

        // when
        val result = updateCommentRoomsUseCase()

        // then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}