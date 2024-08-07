package com.zzang.chongdae.auth.service.dto;

public record SignupTokenResponseItem(String accessToken, String refreshToken) {

    public SignupTokenResponseItem(TokenDto tokenDto) {
        this(tokenDto.accessToken(), tokenDto.refreshToken());
    }
}
