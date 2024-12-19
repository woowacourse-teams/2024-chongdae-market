package com.zzang.chongdae.domain.usecase.comment

import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.CommentRoomsRepositoryQualifier
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import javax.inject.Inject

class UpdateCommentRoomsUseCaseImpl
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        @CommentRoomsRepositoryQualifier private val commentRoomsRepository: CommentRoomsRepository,
    ) : UpdateCommentRoomsUseCase {
        override suspend fun invoke(): Result<List<CommentRoom>, DataError.Network> {
            return when (val result = commentRoomsRepository.fetchCommentRooms()) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            when (authRepository.saveRefresh()) {
                                is Result.Success -> invoke()
                                is Result.Error -> result
                            }
                        }

                        else -> result
                    }
                }
            }
        }
    }
