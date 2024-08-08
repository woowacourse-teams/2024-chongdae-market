package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record ProposerResponseItem(String nickname) {
    public ProposerResponseItem(MemberEntity member) {
        this(member.getNickname());
    }
}
