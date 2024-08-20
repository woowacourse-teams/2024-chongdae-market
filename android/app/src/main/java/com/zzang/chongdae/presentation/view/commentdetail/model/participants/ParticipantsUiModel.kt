package com.zzang.chongdae.presentation.view.commentdetail.model.participants

data class ParticipantsUiModel(
    val proposer: ProposerUiModel,
    val participants: List<ParticipantUiModel>,
    val currentCount: Int,
    val totalCount: Int,
    val price: Int,
)
