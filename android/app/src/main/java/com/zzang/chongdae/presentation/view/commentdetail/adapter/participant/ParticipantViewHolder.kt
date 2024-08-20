package com.zzang.chongdae.presentation.view.commentdetail.adapter.participant

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemOfferingMemberBinding
import com.zzang.chongdae.presentation.view.commentdetail.model.ParticipantUiModel

class ParticipantViewHolder(
    private val binding: ItemOfferingMemberBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(participant: ParticipantUiModel) {
        binding.participant = participant
    }
}
