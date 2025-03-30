package com.zzang.chongdae.presentation.view.commentdetail.model.participants

import com.zzang.chongdae.domain.model.participant.Participant

data class ParticipantUiModel(
    val nickname: String,
    val count: Int,
    val price: Int,
) {
    companion object {
        fun Participant.toUiModel(): ParticipantUiModel {
            return ParticipantUiModel(
                nickname = nickname,
                count = count,
                price = price,
            )
        }
    }
}
