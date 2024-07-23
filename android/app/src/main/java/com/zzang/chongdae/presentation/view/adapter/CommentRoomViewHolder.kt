package com.zzang.chongdae.presentation.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemCommentRoomBinding
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.presentation.view.CommentRoomViewModel

sealed class CommentRoomViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {
    class Representative(
        private val binding: ItemCommentRoomBinding,
    ) : CommentRoomViewHolder(binding.root) {
        fun bind(
            commentRoom: CommentRoom,
            commentRoomViewModel: CommentRoomViewModel,
        ) {
            binding.commentRoom = commentRoom
        }
    }
}
