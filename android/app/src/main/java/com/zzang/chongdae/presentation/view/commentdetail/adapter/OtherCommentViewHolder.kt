package com.zzang.chongdae.presentation.view.commentdetail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemOtherCommentBinding
import com.zzang.chongdae.domain.model.Comment

class OtherCommentViewHolder(
    private val binding: ItemOtherCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
    }
}
