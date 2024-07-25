package com.zzang.chongdae.presentation.view.comment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemCommentRoomBinding
import com.zzang.chongdae.databinding.ItemCommentRoomProposerBinding
import com.zzang.chongdae.domain.model.CommentRoom

sealed class CommentRoomViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {
    class Proposer(
        private val binding: ItemCommentRoomProposerBinding,
    ) : CommentRoomViewHolder(binding.root) {
        fun bind(commentRoom: CommentRoom) {
            binding.commentRoom = commentRoom
        }
    }

    class NotProposer(
        private val binding: ItemCommentRoomBinding,
    ) : CommentRoomViewHolder(binding.root) {
        fun bind(commentRoom: CommentRoom) {
            binding.commentRoom = commentRoom
        }
    }
}
