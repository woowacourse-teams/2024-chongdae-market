package com.zzang.chongdae.presentation.view.commentdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class CommentDetailViewModel(
    private val offeringId: Long,
    private val offeringTitle: String,
) : ViewModel() {
    private val _isCollapsibleViewVisible = MutableLiveData<Boolean>(false)
    val isCollapsibleViewVisible: LiveData<Boolean> get() = _isCollapsibleViewVisible
    
    fun toggleCollapsibleView() {
        _isCollapsibleViewVisible.value = _isCollapsibleViewVisible.value?.not()
        Log.d("CommentDetailViewModel", "toggleCollapsibleView: ${_isCollapsibleViewVisible.value}")
    }
    
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getFactory(offeringId: Long, offeringTitle: String) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T =
                    CommentDetailViewModel(offeringId, offeringTitle) as T
            }
    }
}
