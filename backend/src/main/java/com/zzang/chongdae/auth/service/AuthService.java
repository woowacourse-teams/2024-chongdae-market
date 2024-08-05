package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.TokenDto;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto login(LoginRequest request) {
        String password = passwordEncoder.encode(request.ci());
        MemberEntity member = memberRepository.findByPassword(password)
                .orElseThrow(() -> new MarketException(AuthErrorCode.INVALID_PASSWORD));
        return jwtTokenProvider.createAuthToken(member.getId().toString());
    }
}
