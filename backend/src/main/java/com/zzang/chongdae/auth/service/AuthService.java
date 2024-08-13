package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.SignupMemberDto;
import com.zzang.chongdae.auth.service.dto.SignupRequest;
import com.zzang.chongdae.auth.service.dto.SignupResponseDto;
import com.zzang.chongdae.auth.service.dto.TokenDto;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final NicknameGenerator nickNameGenerator;

    public SignupResponseDto login(LoginRequest request) {
        String password = passwordEncoder.encode(request.ci());
        MemberEntity member = memberRepository.findByPassword(password)
                .orElseThrow(() -> new MarketException(AuthErrorCode.INVALID_PASSWORD));
        return createTokenByMember(member);
    }

    @Transactional
    public SignupResponseDto signup(SignupRequest request) {
        String password = passwordEncoder.encode(request.ci());
        if (memberRepository.existsByPassword(password)) {
            throw new MarketException(AuthErrorCode.DUPLICATED_MEMBER);
        }
        MemberEntity member = new MemberEntity(nickNameGenerator.generate(), password);
        MemberEntity savedMember = memberRepository.save(member);
        return createTokenByMember(savedMember);
    }

    private SignupResponseDto createTokenByMember(MemberEntity member) {
        SignupMemberDto memberDto = new SignupMemberDto(member);
        TokenDto tokenDto = jwtTokenProvider.createAuthToken(member.getId().toString());
        return new SignupResponseDto(memberDto, tokenDto);
    }

    public TokenDto refresh(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        return jwtTokenProvider.createAuthToken(memberId.toString());
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }
}
