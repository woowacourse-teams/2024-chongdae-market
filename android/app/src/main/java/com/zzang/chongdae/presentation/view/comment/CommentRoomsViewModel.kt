package com.zzang.chongdae.presentation.view.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.zzang.chongdae.BuildConfig
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import kotlinx.coroutines.launch

class CommentRoomsViewModel(
    private val commentRoomsRepository: CommentRoomsRepository,
) : ViewModel() {
    private val _commentRooms: MutableLiveData<List<CommentRoom>> = MutableLiveData()
    val commentRooms: LiveData<List<CommentRoom>> get() = _commentRooms

    val isCommentRoomsEmpty: LiveData<Boolean> get() =
        commentRooms.map {
            it.isEmpty()
        }

    fun updateCommentRooms() {
        viewModelScope.launch {
            commentRoomsRepository.fetchCommentRooms(BuildConfig.TOKEN.toLong()).onSuccess {
                _commentRooms.value = it
            }.onFailure {
                Log.e("error", it.message.toString())
            }
        }
    }

    companion object {
        fun factory(commentRoomsRepositoryImpl: CommentRoomsRepository): ViewModelProvider.Factory {
            return CommentRoomsViewModelFactory(commentRoomsRepositoryImpl)
        }
    }
}
