package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.global.exception.MarketException;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class CookieConsumer {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public String getAccessToken(Cookie[] cookies) {
        return getTokenByCookieName(ACCESS_TOKEN_COOKIE_NAME, cookies);
    }

    private String getTokenByCookieName(String cookieName, Cookie[] cookies) {
        if (cookies == null) {
            throw new MarketException(AuthErrorCode.INVALID_TOKEN);
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new MarketException(AuthErrorCode.INVALID_TOKEN));
    }
}
