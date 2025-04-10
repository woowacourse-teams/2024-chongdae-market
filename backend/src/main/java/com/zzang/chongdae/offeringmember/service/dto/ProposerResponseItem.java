package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record ProposerResponseItem(String nickname,
                                   Integer count,
                                   Integer price) {

    public ProposerResponseItem(OfferingMemberEntity offeringMember, int pricePerOne) {
        this(offeringMember.getMember().getNickname(),
                offeringMember.getParticipationCount(),
                offeringMember.getParticipationCount() * pricePerOne);
    }
}
