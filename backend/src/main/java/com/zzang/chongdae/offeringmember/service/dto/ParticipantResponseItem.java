package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record ParticipantResponseItem(String nickname) {

    public ParticipantResponseItem(OfferingMemberEntity offeringMember) {
        this(offeringMember.getMember().getNickname());
    }
}
