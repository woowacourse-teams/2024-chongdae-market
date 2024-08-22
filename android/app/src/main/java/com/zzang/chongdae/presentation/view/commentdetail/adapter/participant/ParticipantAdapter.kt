package com.zzang.chongdae.presentation.view.commentdetail.adapter.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.zzang.chongdae.databinding.ItemOfferingMemberBinding
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantUiModel

class ParticipantAdapter : ListAdapter<ParticipantUiModel, ParticipantViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ParticipantViewHolder {
        val binding =
            ItemOfferingMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParticipantViewHolder,
        position: Int,
    ) {
        val participant = getItem(position)
        holder.bind(participant)
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ParticipantUiModel>() {
                override fun areItemsTheSame(
                    oldItem: ParticipantUiModel,
                    newItem: ParticipantUiModel,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ParticipantUiModel,
                    newItem: ParticipantUiModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
