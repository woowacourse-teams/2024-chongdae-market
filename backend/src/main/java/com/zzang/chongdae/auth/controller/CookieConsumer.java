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

    public void addCookies(HttpServletResponse servletResponse, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            servletResponse.addCookie(cookie);
        }
    }

    public String getTokenByCookieName(String cookieName, Cookie[] cookies) {
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
