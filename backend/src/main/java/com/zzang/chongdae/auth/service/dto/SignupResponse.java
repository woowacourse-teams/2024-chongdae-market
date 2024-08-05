package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupResponse(Long memberId) {

    public SignupResponse(MemberEntity member) {
        this(member.getId());
    }
}
