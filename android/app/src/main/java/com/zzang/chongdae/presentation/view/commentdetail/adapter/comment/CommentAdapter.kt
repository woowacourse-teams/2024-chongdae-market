package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemDateSeparatorBinding
import com.zzang.chongdae.databinding.ItemMyCommentBinding
import com.zzang.chongdae.databinding.ItemOtherCommentBinding
import com.zzang.chongdae.domain.model.Comment

class CommentAdapter : ListAdapter<CommentViewType, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    fun submitComments(comments: List<Comment>) {
        val newItems = mutableListOf<CommentViewType>()

        for (i in comments.indices) {
            val currentComment = comments[i]
            val previousComment = if (i > 0) comments[i - 1] else null

            if (previousComment == null || currentComment.commentCreatedAt.date != previousComment.commentCreatedAt.date) {
                newItems.add(CommentViewType.DateSeparator(currentComment))
            }

            newItems.add(CommentViewType.fromComment(currentComment))
        }

        submitList(newItems)
    }

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

            VIEW_TYPE_DATE_SEPARATOR -> {
                val binding = ItemDateSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateSeparatorViewHolder(binding)
            }

            else -> throw IllegalArgumentException("CommentAdapter viewType error")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is CommentViewType.MyComment -> (holder as MyCommentViewHolder).bind(item.comment)
            is CommentViewType.OtherComment -> (holder as OtherCommentViewHolder).bind(item.comment)
            is CommentViewType.DateSeparator -> (holder as DateSeparatorViewHolder).bind(item.comment)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CommentViewType.MyComment -> VIEW_TYPE_MY_COMMENT
            is CommentViewType.OtherComment -> VIEW_TYPE_OTHER_COMMENT
            is CommentViewType.DateSeparator -> VIEW_TYPE_DATE_SEPARATOR
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_COMMENT = 1
        private const val VIEW_TYPE_OTHER_COMMENT = 2
        private const val VIEW_TYPE_DATE_SEPARATOR = 3

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CommentViewType>() {
                override fun areItemsTheSame(
                    oldItem: CommentViewType,
                    newItem: CommentViewType,
                ): Boolean {
                    return when {
                        oldItem is CommentViewType.MyComment && newItem is CommentViewType.MyComment ->
                            oldItem.comment == newItem.comment

                        oldItem is CommentViewType.OtherComment && newItem is CommentViewType.OtherComment ->
                            oldItem.comment == newItem.comment

                        oldItem is CommentViewType.DateSeparator && newItem is CommentViewType.DateSeparator ->
                            oldItem.comment == newItem.comment

                        else -> false
                    }
                }

                override fun areContentsTheSame(
                    oldItem: CommentViewType,
                    newItem: CommentViewType,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
