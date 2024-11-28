package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public record SignupResponse(SignupMemberResponseItem member, SignupTokenResponseItem token) {

    public SignupResponse(MemberEntity savedMember, TokenDto tokenDto) {
        this(new SignupMemberResponseItem(savedMember), new SignupTokenResponseItem(tokenDto));
    }
}
