package com.zzang.chongdae.domain.model.participant

data class Participants(
    val proposer: Proposer,
    val participants: List<Participant>,
    val participantCount: ParticipantCount,
    val price: Int,
)
