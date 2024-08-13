package com.zzang.chongdae.auth.service.dto;

public record SignupResponse(Long memberId, String nickname) {

    public SignupResponse(SignupMemberDto memberDto) {
        this(memberDto.id(), memberDto.nickname());
    }
}
