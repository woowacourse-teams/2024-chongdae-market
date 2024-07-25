package com.zzang.chongdae.presentation.view.commentdetail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemMyCommentBinding
import com.zzang.chongdae.domain.model.Comment

class MyCommentViewHolder(
    private val binding: ItemMyCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
    }
}
