package com.zzang.chongdae.auth.service.dto;

public record SignupResponse(Long memberId, String nickname) {

    public SignupResponse(SignupOutput output) {
        this(output.member().id(), output.member().nickname());
    }
}
