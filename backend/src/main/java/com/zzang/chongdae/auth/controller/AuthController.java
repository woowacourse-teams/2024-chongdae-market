package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.service.AuthService;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.TokenDto;
import jakarta.servlet.http.Cookie;
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
    ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request, HttpServletResponse servletResponse) {
        TokenDto tokenDto = authService.login(request);
        List<Cookie> cookies = cookieExtractor.extractAuthCookies(tokenDto);
        cookieConsumer.addCookies(servletResponse, cookies);
        return ResponseEntity.ok().build();
    }
}
