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
import com.zzang.chongdae.domain.model.HttpStatusCode
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
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
            commentRoomsRepository.fetchCommentRooms().onSuccess {
                _commentRooms.value = it
            }.onFailure {
                Log.e("error", "updateCommentRooms: ${it.message}")
                when (it.message) {
                    HttpStatusCode.UNAUTHORIZED_401.code -> {
                        Log.e("error", "Access Token 만료")
                        authRepository.saveRefresh()
                        updateCommentRooms()
                    }
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(
            authRepository: AuthRepository,
            commentRoomsRepository: CommentRoomsRepository,
        ) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CommentRoomsViewModel(authRepository, commentRoomsRepository) as T
                }
            }
    }
}
