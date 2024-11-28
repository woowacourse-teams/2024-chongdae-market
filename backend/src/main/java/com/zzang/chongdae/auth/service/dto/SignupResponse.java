package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupResponse(Long memberId, String nickname) {

    public SignupResponse(MemberEntity member) {
        this(member.getId(), member.getNickname());
    }
}
