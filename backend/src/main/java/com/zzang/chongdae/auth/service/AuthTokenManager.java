package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.repository.RefreshTokenRepository;
import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AuthTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @WriterDatabase
    @Transactional
    public AuthTokenDto createToken(MemberEntity member) {
        String deviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString(), deviceId);
        RefreshTokenEntity auth = new RefreshTokenEntity(member, deviceId, authToken.refreshToken());
        refreshTokenRepository.save(auth);
        return authToken;
    }

    @WriterDatabase
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(String refreshToken) {
        MemberEntity member = findMemberByRefreshToken(refreshToken);
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken);
        if (isValidLegacy(member, refreshToken)) {
            return true;
        }
        RefreshTokenEntity refreshTokenEntity = findRefreshTokenFromRepository(member, deviceId);
        if (!refreshTokenEntity.isValid(refreshToken)) {
            refreshTokenRepository.delete(refreshTokenEntity);
            return false;
        }
        return true;
    }

    private MemberEntity findMemberByRefreshToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }

    private boolean isValidLegacy(MemberEntity member, String refreshToken) {
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken);
        return deviceId == null && !refreshTokenRepository.existsByMemberAndDeviceId(member, deviceId);
    }

    private RefreshTokenEntity findRefreshTokenFromRepository(MemberEntity member, String deviceId) {
        return refreshTokenRepository.findByMemberAndDeviceId(member, deviceId)
                .orElseThrow(() -> new MarketException(AuthErrorCode.EXPIRED_REFRESH_TOKEN));
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refresh(String refreshToken) {
        MemberEntity member = findMemberByRefreshToken(refreshToken);
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken);
        if (deviceId == null) {
            return refreshLegacyToken(member, deviceId, refreshToken);
        }
        RefreshTokenEntity refreshTokenEntity = findRefreshTokenFromRepository(member, deviceId);
        AuthTokenDto authTokenDto = jwtTokenProvider.createAuthToken(member.getId().toString(), deviceId);
        refreshTokenEntity.refresh(authTokenDto.refreshToken());
        return authTokenDto;
    }

    private AuthTokenDto refreshLegacyToken(MemberEntity member, String deviceId, String refreshToken) {
        if (refreshTokenRepository.existsByMemberAndDeviceId(member, deviceId)) {
            throw new MarketException(AuthErrorCode.REFRESH_REUSE_EXCEPTION);
        }
        RefreshTokenEntity legacyAuth = new RefreshTokenEntity(member, deviceId, refreshToken);
        refreshTokenRepository.save(legacyAuth);
        return createToken(member);
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }
}
