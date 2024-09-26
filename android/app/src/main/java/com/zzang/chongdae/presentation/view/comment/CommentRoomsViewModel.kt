package com.zzang.chongdae.presentation.view.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.AuthRepositoryQualifier
import com.zzang.chongdae.di.annotations.CommentRoomsRepositoryQualifier
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentRoomsViewModel
    @Inject
    constructor(
        @AuthRepositoryQualifier private val authRepository: AuthRepository,
        @CommentRoomsRepositoryQualifier private val commentRoomsRepository: CommentRoomsRepository,
    ) : ViewModel() {
        private val _commentRooms: MutableLiveData<List<CommentRoom>> = MutableLiveData()
        val commentRooms: LiveData<List<CommentRoom>> get() = _commentRooms

        val isCommentRoomsEmpty: LiveData<Boolean>
            get() =
                commentRooms.map {
                    it.isEmpty()
                }

        fun updateCommentRooms() {
            viewModelScope.launch {
                when (val result = commentRoomsRepository.fetchCommentRooms()) {
                    is Result.Error -> {
                        Log.e("error", "updateCommentRooms: ${result.error}")
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                when (authRepository.saveRefresh()) {
                                    is Result.Success -> updateCommentRooms()
                                    is Result.Error -> return@launch
                                }
                            }
                            else -> {}
                        }
                    }
                    is Result.Success -> _commentRooms.value = result.data
                }
            }
        }
    }
