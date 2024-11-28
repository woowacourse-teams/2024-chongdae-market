package com.zzang.chongdae.presentation.view.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zzang.chongdae.domain.repository.CommentRoomsRepository

class CommentRoomsViewModelFactory(
    private val commentRoomsRepository: CommentRoomsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentRoomsViewModel(commentRoomsRepository) as T
    }
}
