package com.zzang.chongdae.presentation.view.commentdetail.model.participants

import com.zzang.chongdae.domain.model.participant.Proposer

data class ProposerUiModel(
    val nickname: String,
) {
    companion object {
        fun Proposer.toUiModel(): ProposerUiModel {
            return ProposerUiModel(
                nickname = nickname,
            )
        }
    }
}
