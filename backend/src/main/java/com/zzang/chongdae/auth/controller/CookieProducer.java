package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CookieProducer {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public List<Cookie> extractAuthCookies(AuthTokenDto authToken) {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(createCookie(ACCESS_TOKEN_COOKIE_NAME, authToken.accessToken()));
        cookies.add(createCookie(REFRESH_TOKEN_COOKIE_NAME, authToken.refreshToken()));
        return cookies;
    }

    private Cookie createCookie(String tokenName, String token) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
