package com.zzang.chongdae.presentation.view.commentdetail.model.participants

import com.zzang.chongdae.domain.model.participant.Participant

data class ParticipantUiModel(
    val nickname: String,
) {
    companion object {
        fun Participant.toUiModel(): ParticipantUiModel {
            return ParticipantUiModel(
                nickname = nickname,
            )
        }
    }
}
