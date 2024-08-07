package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupResponseDto(SignupMemberDto member, TokenDto token) {

    public SignupResponseDto(MemberEntity member, TokenDto token) {
        this(new SignupMemberDto(member), token);
    }
}
