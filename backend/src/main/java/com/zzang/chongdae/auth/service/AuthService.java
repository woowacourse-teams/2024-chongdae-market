package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.SignupRequest;
import com.zzang.chongdae.auth.service.dto.SignupResponse;
import com.zzang.chongdae.auth.service.dto.TokenDto;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String password = passwordEncoder.encode(request.ci());
        if (memberRepository.existsByPassword(password)) {
            throw new MarketException(AuthErrorCode.DUPLICATED_MEMBER);
        }
        MemberEntity member = new MemberEntity("dora2", password);
        MemberEntity savedMember = memberRepository.save(member);
        return new SignupResponse(savedMember);
    }
}
