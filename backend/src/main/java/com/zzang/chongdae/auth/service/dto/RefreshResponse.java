package com.zzang.chongdae.auth.service.dto;

public record RefreshResponse(String accessToken, String refreshToken) {

    public RefreshResponse(TokenDto tokenDto) {
        this(tokenDto.accessToken(), tokenDto.refreshToken());
    }
}
