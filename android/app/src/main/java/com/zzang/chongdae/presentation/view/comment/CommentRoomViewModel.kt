package com.zzang.chongdae.presentation.view.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomRepository
import kotlinx.coroutines.launch

class CommentRoomViewModel(
    private val commentRoomRepository: CommentRoomRepository,
) : ViewModel() {
    private val _commentRooms: MutableLiveData<List<CommentRoom>> = MutableLiveData()
    val commentRooms: LiveData<List<CommentRoom>> get() = _commentRooms

    fun updateCommentRooms() {
        viewModelScope.launch {
            commentRoomRepository.fetchCommentRoom(1).onSuccess {
                _commentRooms.value = it
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    fun putCommentRooms(commentRooms: List<CommentRoom>) {
        _commentRooms.value = commentRooms
    }

    companion object {
        fun factory(commentRoomRepositoryImpl: CommentRoomRepository): ViewModelProvider.Factory {
            return CommentRoomViewModelFactory(commentRoomRepositoryImpl)
        }
    }
}
