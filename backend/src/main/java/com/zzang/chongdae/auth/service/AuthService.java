package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.domain.AuthToken;
import com.zzang.chongdae.auth.domain.KakaoMemberInfo;
import com.zzang.chongdae.auth.domain.LoginMember;
import com.zzang.chongdae.auth.domain.SignupMember;
import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.NicknameGenerator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final NicknameGenerator nickNameGenerator;
    private final AuthClient authClient;

    @WriterDatabase
    public AuthInfoDto kakaoLogin(KakaoLoginRequest request) {
        KakaoMemberInfo kakaoMemberInfo = authClient.getKakaoMemberInfo(request.accessToken());
        MemberEntity member = memberRepository.findByLoginId(kakaoMemberInfo.getLoginId())
                .orElseGet(() -> signup(kakaoMemberInfo)); // adaptor로 이동
        return login(member.toLoginMember()); // adaptor로 이동
    }

    private MemberEntity signup(KakaoMemberInfo kakaoMemberInfo) {
        String nickname = nickNameGenerator.generate();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        SignupMember signupMember = new SignupMember(nickname, password, kakaoMemberInfo);
        MemberEntity member = new MemberEntity(signupMember); // adaptor로 이동
        return memberRepository.save(member); // adaptor로 이동
    }

    private AuthInfoDto login(LoginMember loginMember) {
        AuthToken authToken = jwtTokenProvider.createAuthToken(loginMember.getId().toString());
        return new AuthInfoDto(loginMember, authToken);
    }

    public AuthToken refresh(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        return jwtTokenProvider.createAuthToken(memberId.toString());
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND)); // TODO: 전부 도메인으로 교체
    }
}
