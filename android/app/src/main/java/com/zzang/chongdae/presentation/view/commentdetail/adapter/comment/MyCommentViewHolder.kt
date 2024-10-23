package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemMyCommentBinding
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel

class MyCommentViewHolder(
    private val binding: ItemMyCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: CommentUiModel) {
        binding.comment = comment
    }
}
