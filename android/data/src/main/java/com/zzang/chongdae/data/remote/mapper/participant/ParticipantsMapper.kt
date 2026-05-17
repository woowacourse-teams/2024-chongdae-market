package com.zzang.chongdae.data.remote.mapper.participant

import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse
import com.zzang.chongdae.data.remote.dto.response.participants.RemoteCount
import com.zzang.chongdae.data.remote.dto.response.participants.RemoteParticipant
import com.zzang.chongdae.data.remote.dto.response.participants.RemoteProposer
import com.zzang.chongdae.domain.model.participant.Participant
import com.zzang.chongdae.domain.model.participant.ParticipantCount
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.model.participant.Proposer

fun com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse.toDomain(): Participants {
    return Participants(
        proposer = this.remoteProposer.toDomain(),
        participants = this.participants.map { it.toDomain() },
        participantCount = this.remoteCount.toDomain(),
        price = this.price,
    )
}

fun com.zzang.chongdae.data.remote.dto.response.participants.RemoteProposer.toDomain(): Proposer {
    return Proposer(
        nickname = this.nickname,
        count = this.count,
        price = this.price,
    )
}

fun com.zzang.chongdae.data.remote.dto.response.participants.RemoteParticipant.toDomain(): Participant {
    return Participant(
        nickname = this.nickname,
        count = this.count,
        price = this.price,
    )
}

fun com.zzang.chongdae.data.remote.dto.response.participants.RemoteCount.toDomain(): ParticipantCount {
    return ParticipantCount(
        totalCount = this.totalCount,
        currentCount = this.currentCount,
    )
}
