package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemDateSeparatorBinding
import com.zzang.chongdae.databinding.ItemMyCommentBinding
import com.zzang.chongdae.databinding.ItemOtherCommentBinding
import com.zzang.chongdae.presentation.view.commentdetail.model.comment.CommentUiModel

class CommentAdapter : ListAdapter<CommentUiModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_COMMENT -> {
                val binding =
                    ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyCommentViewHolder(binding)
            }

            VIEW_TYPE_OTHER_COMMENT -> {
                val binding =
                    ItemOtherCommentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                OtherCommentViewHolder(binding)
            }

            VIEW_TYPE_DATE_SEPARATOR -> {
                val binding =
                    ItemDateSeparatorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                DateSeparatorViewHolder(binding)
            }

            else -> throw IllegalArgumentException("CommentAdapter viewType error")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is MyCommentViewHolder -> holder.bind(getItem(position))
            is OtherCommentViewHolder -> holder.bind(getItem(position))
            is DateSeparatorViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).commentViewType) {
            CommentViewType.MyComment -> VIEW_TYPE_MY_COMMENT
            CommentViewType.OtherComment -> VIEW_TYPE_OTHER_COMMENT
            CommentViewType.DateSeparator -> VIEW_TYPE_DATE_SEPARATOR
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_COMMENT = 1
        private const val VIEW_TYPE_OTHER_COMMENT = 2
        private const val VIEW_TYPE_DATE_SEPARATOR = 3

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CommentUiModel>() {
                override fun areItemsTheSame(
                    oldItem: CommentUiModel,
                    newItem: CommentUiModel,
                ): Boolean {
                    return oldItem.commentViewType == newItem.commentViewType &&
                        oldItem.date == newItem.date && oldItem.time == newItem.time
                }

                override fun areContentsTheSame(
                    oldItem: CommentUiModel,
                    newItem: CommentUiModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
