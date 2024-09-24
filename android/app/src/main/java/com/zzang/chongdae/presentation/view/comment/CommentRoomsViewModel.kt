package com.zzang.chongdae.presentation.view.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import kotlinx.coroutines.launch

class CommentRoomsViewModel(
    private val authRepository: AuthRepository,
    private val commentRoomsRepository: CommentRoomsRepository,
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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            authRepository: AuthRepository,
            commentRoomsRepository: CommentRoomsRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CommentRoomsViewModel(authRepository, commentRoomsRepository) as T
            }
        }
    }
}
