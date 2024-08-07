package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupOutput(SignupMemberDto member, TokenDto token) {

    public SignupOutput(MemberEntity member, TokenDto token) {
        this(new SignupMemberDto(member), token);
    }
}
