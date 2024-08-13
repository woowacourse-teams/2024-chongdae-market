package com.zzang.chongdae.auth.service.dto;

public record SignupResponse(Long memberId, String nickname) {

    public SignupResponse(AuthMemberDto authMember) {
        this(authMember.id(), authMember.nickname());
    }
}
