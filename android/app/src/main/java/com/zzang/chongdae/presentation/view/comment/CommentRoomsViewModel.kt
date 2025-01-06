package com.zzang.chongdae.presentation.view.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.di.annotations.UpdateCommentRoomsUseCaseQualifier
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.usecase.comment.UpdateCommentRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentRoomsViewModel
    @Inject
    constructor(
        @UpdateCommentRoomsUseCaseQualifier private val updateCommentRoomsUseCase: UpdateCommentRoomsUseCase,
    ) : ViewModel() {
        private val _commentRooms: MutableLiveData<List<CommentRoom>> = MutableLiveData()
        val commentRooms: LiveData<List<CommentRoom>> get() = _commentRooms

        val isCommentRoomsEmpty: LiveData<Boolean>
            get() = commentRooms.map { it.isEmpty() }

        fun updateCommentRooms() {
            viewModelScope.launch {
                when (val result = updateCommentRoomsUseCase()) {
                    is Result.Success -> _commentRooms.value = result.data
                    is Result.Error -> Log.e("error", "updateCommentRooms: ${result.error}")
                }
            }
        }
    }
