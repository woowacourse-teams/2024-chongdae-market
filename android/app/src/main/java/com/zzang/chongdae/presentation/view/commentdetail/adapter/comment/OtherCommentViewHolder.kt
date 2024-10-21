package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemOtherCommentBinding
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel

class OtherCommentViewHolder(
    private val binding: ItemOtherCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: CommentUiModel) {
        binding.comment = comment
    }
}
