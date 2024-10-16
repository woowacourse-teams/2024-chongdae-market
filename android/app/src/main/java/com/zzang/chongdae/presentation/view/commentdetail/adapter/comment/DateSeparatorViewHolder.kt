package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemDateSeparatorBinding
import com.zzang.chongdae.domain.model.Comment

class DateSeparatorViewHolder(
    private val binding: ItemDateSeparatorBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
    }
}
