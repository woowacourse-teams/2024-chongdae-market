package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupResponse(SignupMemberResponseItem member, TokenDto token) {

    public SignupResponse(MemberEntity savedMember, TokenDto tokenDto) {
        this(new SignupMemberResponseItem(savedMember), tokenDto);
    }
}
