package com.zzang.chongdae.global.helper;

import com.zzang.chongdae.auth.repository.RefreshTokenRepository;
import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import com.zzang.chongdae.auth.service.JwtTokenProvider;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    public CookieProvider(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Cookies createCookiesWithMember(MemberEntity member) {
        String deviceId = UUID.randomUUID().toString();
        return this.createCookiesWithMember(member, deviceId);
    }

    public Cookies createCookiesWithMember(MemberEntity member, String deviceId) {
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString(), deviceId);
        RefreshTokenEntity auth = new RefreshTokenEntity(member.getId(), deviceId, authToken.refreshToken());
        refreshTokenRepository.save(auth);
        Cookie accessTokenCookie = new Cookie.Builder(ACCESS_TOKEN_COOKIE_NAME, authToken.accessToken()).build();
        Cookie refreshTokenCookie = new Cookie.Builder(REFRESH_TOKEN_COOKIE_NAME, authToken.refreshToken()).build();
        return new Cookies(accessTokenCookie, refreshTokenCookie);
    }
}
