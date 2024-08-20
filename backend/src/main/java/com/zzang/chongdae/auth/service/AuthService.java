package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.AuthMemberDto;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.auth.service.dto.LoginRequest;
import com.zzang.chongdae.auth.service.dto.SignupRequest;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.domain.AuthProvider;
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
    private final AuthClient authClient;

    public AuthInfoDto login(LoginRequest request) {
        String password = passwordEncoder.encode(request.ci());
        MemberEntity member = memberRepository.findByPassword(password)
                .orElseThrow(() -> new MarketException(AuthErrorCode.INVALID_PASSWORD));
        return login(member);
    }

    @Transactional
    public AuthInfoDto signup(SignupRequest request) {
        String password = passwordEncoder.encode(request.ci());
        if (memberRepository.existsByPassword(password)) {
            throw new MarketException(AuthErrorCode.DUPLICATED_MEMBER);
        }
        MemberEntity member = new MemberEntity(nickNameGenerator.generate(), password);
        MemberEntity savedMember = memberRepository.save(member);
        return login(savedMember);
    }

    public AuthInfoDto kakaoLogin(KakaoLoginRequest request) {
        String loginId = authClient.getKakaoUserInfo(request.accessToken());
        AuthProvider provider = AuthProvider.KAKAO;
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseGet(() -> signup(provider, loginId));
        return login(member);
    }

    private MemberEntity signup(AuthProvider provider, String loginId) {
        MemberEntity member = new MemberEntity(nickNameGenerator.generate(), provider, loginId);
        return memberRepository.save(member);
    }

    private AuthInfoDto login(MemberEntity member) {
        AuthMemberDto authMember = new AuthMemberDto(member);
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString());
        return new AuthInfoDto(authMember, authToken);
    }

    public AuthTokenDto refresh(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        return jwtTokenProvider.createAuthToken(memberId.toString());
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }
}
