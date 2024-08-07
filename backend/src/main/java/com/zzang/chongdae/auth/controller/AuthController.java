package com.zzang.chongdae.auth.controller;

import com.zzang.chongdae.auth.service.AuthService;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.LoginResponse;
import com.zzang.chongdae.auth.service.dto.RefreshRequest;
import com.zzang.chongdae.auth.service.dto.RefreshResponse;
import com.zzang.chongdae.auth.service.dto.SignupRequest;
import com.zzang.chongdae.auth.service.dto.SignupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @RequestBody RefreshRequest request) {
        RefreshResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }
}
