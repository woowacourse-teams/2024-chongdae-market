package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.global.exception.MarketException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CookieConsumer {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public void addCookies(HttpServletResponse servletResponse, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            servletResponse.addCookie(cookie);
        }
    }

    public String getAccessToken(Cookie[] cookies) {
        return getTokenByCookieName(ACCESS_TOKEN_COOKIE_NAME, cookies);
    }

    public String getRefreshToken(Cookie[] cookies) {
        return getTokenByCookieName(REFRESH_TOKEN_COOKIE_NAME, cookies);
    }

    private String getTokenByCookieName(String cookieName, Cookie[] cookies) {
        if (cookies == null) {
            throw new MarketException(AuthErrorCode.COOKIE_NOT_EXIST);
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new MarketException(AuthErrorCode.INVALID_COOKIE));
    }
}
