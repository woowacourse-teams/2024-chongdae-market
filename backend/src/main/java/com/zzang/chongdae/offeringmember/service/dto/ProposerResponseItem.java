package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record ProposerResponseItem(String nickname){
    public ProposerResponseItem(OfferingMemberEntity member) {
        this(member.getMember().getNickname());
    }
}
