package com.zzang.chongdae.auth.service.dto;

public record LoginResponse(Long memberId, String nickname) {

    public LoginResponse(AuthMemberDto authMember) {
        this(authMember.id(), authMember.nickname());
    }
}
