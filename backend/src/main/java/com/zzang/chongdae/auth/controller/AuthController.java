package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.service.AuthService;
import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.auth.service.dto.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieProducer cookieExtractor;
    private final CookieConsumer cookieConsumer;

    @PostMapping("/auth/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @RequestBody @Valid KakaoLoginRequest request, HttpServletResponse servletResponse) {
        AuthInfoDto authInfo = authService.kakaoLogin(request);
        addTokenToHttpServletResponse(authInfo.authToken(), servletResponse);
        LoginResponse response = new LoginResponse(authInfo.authMember());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String refreshToken = cookieConsumer.getRefreshToken(servletRequest.getCookies());
        AuthTokenDto authToken = authService.refresh(refreshToken);
        addTokenToHttpServletResponse(authToken, servletResponse);
        return ResponseEntity.ok().build();
    }

    private void addTokenToHttpServletResponse(AuthTokenDto authToken, HttpServletResponse servletResponse) {
        List<Cookie> cookies = cookieExtractor.extractAuthCookies(authToken);
        cookieConsumer.addCookies(servletResponse, cookies);
    }
}
