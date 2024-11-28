package com.zzang.chongdae.auth.service.dto;

public record LoginResponse(String accessToken, String refreshToken) {

    public LoginResponse(TokenDto tokenDto) {
        this(tokenDto.accessToken(), tokenDto.refreshToken());
    }
}
