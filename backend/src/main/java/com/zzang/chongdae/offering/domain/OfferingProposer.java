package com.zzang.chongdae.offering.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OfferingProposer {

    private final MemberEntity member;

    public boolean isProposer(MemberEntity other) {
        return this.member.equals(other);
    }
}
