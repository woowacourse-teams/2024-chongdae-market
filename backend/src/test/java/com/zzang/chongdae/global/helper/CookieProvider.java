package com.zzang.chongdae.global.helper;

import com.zzang.chongdae.auth.service.dto.TokenDto;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CookieProvider {

    private final TestTokenProvider tokenProvider;

    @Autowired
    public CookieProvider(TestTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public Cookies createCookies() {
        TokenDto tokenDto = tokenProvider.createTokens();
        return createCookiesFromTokenDto(tokenDto);
    }

    public Cookies createCookiesWithCi(String ci) {
        TokenDto tokenDto = tokenProvider.createTokensWithCi(ci);
        return createCookiesFromTokenDto(tokenDto);
    }

    private Cookies createCookiesFromTokenDto(TokenDto tokenDto) {
        Cookie accessToken = new Cookie.Builder("access_token", tokenDto.accessToken()).build();
        Cookie refreshToken = new Cookie.Builder("refresh_token", tokenDto.refreshToken()).build();
        return new Cookies(accessToken, refreshToken);
    }
}
