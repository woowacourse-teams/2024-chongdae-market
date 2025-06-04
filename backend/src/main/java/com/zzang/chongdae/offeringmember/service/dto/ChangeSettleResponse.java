package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;

public record ChangeSettleResponse(Long offeringId, Long memberId, Boolean isSettled) {
    public ChangeSettleResponse(OfferingMemberEntity offeringMember) {
        this(offeringMember.getOffering().getId(),
                offeringMember.getMember().getId(),
                offeringMember.getIsSettled());
    }
}
