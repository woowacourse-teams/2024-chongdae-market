package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.service.AuthService;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.SignupRequest;
import com.zzang.chongdae.auth.service.dto.SignupResponse;
import com.zzang.chongdae.auth.service.dto.TokenDto;
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

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request, HttpServletResponse servletResponse) {
        TokenDto tokenDto = authService.login(request);
        List<Cookie> cookies = cookieExtractor.extractAuthCookies(tokenDto);
        cookieConsumer.addCookies(servletResponse, cookies);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody SignupRequest request, HttpServletResponse servletResponse) {
        SignupResponse response = authService.signup(request); // TODO: serviceDTO로 사용하고, 별도의 controllerDTO 만들기
        addTokenToHttpServletResponse(response.token(), servletResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String refreshToken = cookieConsumer.getRefreshToken(servletRequest.getCookies());
        TokenDto tokenDto = authService.refresh(refreshToken);
        addTokenToHttpServletResponse(tokenDto, servletResponse);
        return ResponseEntity.ok().build();
    }

    private void addTokenToHttpServletResponse(TokenDto tokenDto, HttpServletResponse servletResponse) {
        List<Cookie> cookies = cookieExtractor.extractAuthCookies(tokenDto);
        cookieConsumer.addCookies(servletResponse, cookies);
    }
}
