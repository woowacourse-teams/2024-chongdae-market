package com.zzang.chongdae.presentation.view.commentdetail.model.participants

import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ParticipantUiModel.Companion.toUiModel
import com.zzang.chongdae.presentation.view.commentdetail.model.participants.ProposerUiModel.Companion.toUiModel

data class ParticipantsUiModel(
    val proposer: ProposerUiModel,
    val participants: List<ParticipantUiModel>,
    val currentCount: Int,
    val totalCount: Int,
    val price: Int,
) {
    companion object {
        fun Participants.toUiModel(): ParticipantsUiModel {
            return ParticipantsUiModel(
                proposer = proposer.toUiModel(),
                participants = participants.map { it.toUiModel() },
                currentCount = participantCount.currentCount,
                totalCount = participantCount.totalCount,
                price = price,
            )
        }
    }
}
