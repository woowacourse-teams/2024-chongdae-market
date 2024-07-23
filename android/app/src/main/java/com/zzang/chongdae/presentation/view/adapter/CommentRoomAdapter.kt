package com.zzang.chongdae.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.zzang.chongdae.databinding.ItemCommentRoomBinding
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.presentation.view.CommentRoomViewModel

class CommentRoomAdapter(
    private val commentRoomViewModel: CommentRoomViewModel,
) : ListAdapter<CommentRoom, CommentRoomViewHolder.Representative>(productComparator) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentRoomViewHolder.Representative {
        val binding =
            ItemCommentRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return CommentRoomViewHolder.Representative(binding)
    }

    override fun onBindViewHolder(
        holder: CommentRoomViewHolder.Representative,
        position: Int,
    ) {
        holder.bind(currentList[position], commentRoomViewModel)
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
