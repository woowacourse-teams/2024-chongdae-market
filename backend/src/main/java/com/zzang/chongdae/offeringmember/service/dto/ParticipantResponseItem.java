package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record ParticipantResponseItem(Long memberId,
                                      String nickname,
                                      Integer count,
                                      Integer price,
                                      Boolean isSettled) {

    public ParticipantResponseItem(OfferingMemberEntity offeringMember, int pricePerOne) {
        this(offeringMember.getId(),
                offeringMember.getMember().getNickname(),
                offeringMember.getParticipationCount(),
                offeringMember.getParticipationCount() * pricePerOne,
                offeringMember.getIsSettled());
    }
}
