package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupMemberDto(Long id, String nickname) {

    public SignupMemberDto(MemberEntity member) {
        this(member.getId(), member.getNickname());
    }
}
