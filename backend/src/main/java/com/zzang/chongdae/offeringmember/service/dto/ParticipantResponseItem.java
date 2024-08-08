package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record ParticipantResponseItem(String nickname) {

    public ParticipantResponseItem(MemberEntity offeringMember) {
        this(offeringMember.getNickname());
    }
}
