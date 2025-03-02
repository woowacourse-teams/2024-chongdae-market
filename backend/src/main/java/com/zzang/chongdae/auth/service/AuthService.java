package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.AuthMemberDto;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.event.domain.LoginEvent;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.domain.AuthProvider;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.member.service.NicknameGenerator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final NicknameGenerator nickNameGenerator;
    private final AuthClient authClient;

    @WriterDatabase
    @Transactional
    public AuthInfoDto kakaoLogin(KakaoLoginRequest request) {
        String loginId = authClient.getUserInfo(request.accessToken());
        AuthProvider provider = AuthProvider.KAKAO;
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseGet(() -> signup(provider, loginId, request.fcmToken()));
        return login(member, request.fcmToken());
    }

    private MemberEntity signup(AuthProvider provider, String loginId, String fcmToken) {
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        MemberEntity member = createMember(provider, loginId, fcmToken, password);
        return memberRepository.save(member);
    }

    private MemberEntity createMember(AuthProvider provider, String loginId, String fcmToken, String password) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            return new MemberEntity(nickNameGenerator.generate(), provider, loginId, password);
        }
        return new MemberEntity(nickNameGenerator.generate(), provider, loginId, password, fcmToken);
    }

    private AuthInfoDto login(MemberEntity member, String fcmToken) {
        AuthMemberDto authMember = new AuthMemberDto(member);
        String deviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString(), deviceId);
        checkFcmToken(member, fcmToken);
        eventPublisher.publishEvent(new LoginEvent(this, member));
        return new AuthInfoDto(authMember, authToken);
    }

    private void checkFcmToken(MemberEntity member, String fcmToken) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            member.updateFcmTokenDefault();
            return;
        }
        if (!memberRepository.existsByIdAndFcmToken(member.getId(), fcmToken)) {
            log.info("토큰 갱신 사용자 id: {}", member.getId());
            member.updateFcmToken(fcmToken);
        }
    }

    public AuthTokenDto refresh(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken);

        // TODO: check reuse token
        if (deviceId == null) { // TODO: support legacy request
            // TODO: save null device for check reuse
            // TODO: create new device Id
            deviceId = UUID.randomUUID().toString();
        }
        AuthTokenDto tokens = jwtTokenProvider.createAuthToken(memberId.toString(), deviceId);

        return tokens;
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }
}
