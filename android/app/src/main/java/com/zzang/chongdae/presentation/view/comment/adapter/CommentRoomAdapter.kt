package com.zzang.chongdae.presentation.view.comment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.zzang.chongdae.databinding.ItemCommentRoomBinding
import com.zzang.chongdae.databinding.ItemCommentRoomProposerBinding
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.model.CommentRoomType

class CommentRoomAdapter(
    private val onClickListener: OnCommentRoomClickListener,
) : ListAdapter<CommentRoom, CommentRoomViewHolder>(productComparator) {
    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isProposer == true) {
            CommentRoomType.PROPOSER.separator
        } else {
            CommentRoomType.NOT_PROPOSER.separator
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentRoomViewHolder {
        when (viewType) {
            CommentRoomType.PROPOSER.separator -> {
                val binding =
                    ItemCommentRoomProposerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                return CommentRoomViewHolder.Proposer(binding)
            }

            CommentRoomType.NOT_PROPOSER.separator -> {
                val binding =
                    ItemCommentRoomBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                return CommentRoomViewHolder.NotProposer(binding)
            }

            else -> error("Invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: CommentRoomViewHolder,
        position: Int,
    ) {
        when (holder) {
            is CommentRoomViewHolder.Proposer ->
                holder.bind(
                    currentList[position],
                    onClickListener,
                )

            is CommentRoomViewHolder.NotProposer ->
                holder.bind(
                    currentList[position],
                    onClickListener,
                )
        }
    }

    companion object {
        private val productComparator =
            object : DiffUtil.ItemCallback<CommentRoom>() {
                override fun areItemsTheSame(
                    oldItem: CommentRoom,
                    newItem: CommentRoom,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CommentRoom,
                    newItem: CommentRoom,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
