package com.zzang.chongdae.presentation.view.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zzang.chongdae.domain.repository.CommentRoomRepository

class CommentRoomViewModelFactory(
    private val commentRoomRepository: CommentRoomRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentRoomViewModel(commentRoomRepository) as T
    }
}
