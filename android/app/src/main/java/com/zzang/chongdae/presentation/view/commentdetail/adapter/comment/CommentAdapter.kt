package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemMyCommentBinding
import com.zzang.chongdae.databinding.ItemOtherCommentBinding
import com.zzang.chongdae.domain.model.Comment

class CommentAdapter : ListAdapter<Comment, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_COMMENT -> {
                val binding = ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyCommentViewHolder(binding)
            }

            VIEW_TYPE_OTHER_COMMENT -> {
                val binding = ItemOtherCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherCommentViewHolder(binding)
            }

            else -> throw IllegalArgumentException("CommentAdapter viewType error")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val commentViewType = CommentViewType.fromComment(getItem(position))) {
            is CommentViewType.MyComment ->
                (holder as MyCommentViewHolder).bind(
                    commentViewType.comment,
                )

            is CommentViewType.OtherComment ->
                (holder as OtherCommentViewHolder).bind(
                    commentViewType.comment,
                )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).isMine) {
            true -> VIEW_TYPE_MY_COMMENT
            false -> VIEW_TYPE_OTHER_COMMENT
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_COMMENT = 1
        private const val VIEW_TYPE_OTHER_COMMENT = 2

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Comment>() {
                override fun areItemsTheSame(
                    oldItem: Comment,
                    newItem: Comment,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Comment,
                    newItem: Comment,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
