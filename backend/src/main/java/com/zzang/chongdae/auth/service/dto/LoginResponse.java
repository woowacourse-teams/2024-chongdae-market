package com.zzang.chongdae.auth.service.dto;

import com.zzang.chongdae.auth.domain.LoginMember;

public record LoginResponse(Long memberId, String nickname) {

    public LoginResponse(LoginMember loginMember) {
        this(loginMember.getId(), loginMember.getNickname());
    }
}
