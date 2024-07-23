package com.zzang.chongdae.presentation.view.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zzang.chongdae.domain.model.CommentRoom
import java.time.LocalTime

class CommentRoomViewModel : ViewModel() {
    private val _commentRooms: MutableLiveData<List<CommentRoom>> = MutableLiveData()
    val commentRooms: LiveData<List<CommentRoom>> get() = _commentRooms

    init {
        _commentRooms.value = listOf(CommentRoom(1, "알송", "알송이에용", LocalTime.now()))
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return CommentRoomViewModelFactory()
        }
    }
}
