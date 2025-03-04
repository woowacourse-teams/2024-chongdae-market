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
        String sessionId = UUID.randomUUID().toString();
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString(), sessionId);
        RefreshTokenEntity auth = new RefreshTokenEntity(member, sessionId, authToken.refreshToken());
        refreshTokenRepository.save(auth);
        return authToken;
    }

    @WriterDatabase
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(String refreshToken) {
        MemberEntity member = findMemberByRefreshToken(refreshToken);
        String sessionId = jwtTokenProvider.getSessionIdByRefreshToken(refreshToken);
        if (isValidLegacy(member, refreshToken)) {
            return true;
        }
        RefreshTokenEntity refreshTokenEntity = findRefreshTokenFromRepository(member, sessionId);
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
        String sessionId = jwtTokenProvider.getSessionIdByRefreshToken(refreshToken);
        return sessionId == null && !refreshTokenRepository.existsByMemberAndSessionId(member, sessionId);
    }

    private RefreshTokenEntity findRefreshTokenFromRepository(MemberEntity member, String sessionId) {
        return refreshTokenRepository.findByMemberAndSessionId(member, sessionId)
                .orElseThrow(() -> new MarketException(AuthErrorCode.EXPIRED_REFRESH_TOKEN));
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refresh(String refreshToken) {
        MemberEntity member = findMemberByRefreshToken(refreshToken);
        String sessionId = jwtTokenProvider.getSessionIdByRefreshToken(refreshToken);
        if (sessionId == null) {
            return refreshLegacyToken(member, sessionId, refreshToken);
        }
        RefreshTokenEntity refreshTokenEntity = findRefreshTokenFromRepository(member, sessionId);
        AuthTokenDto authTokenDto = jwtTokenProvider.createAuthToken(member.getId().toString(), sessionId);
        refreshTokenEntity.refresh(authTokenDto.refreshToken());
        return authTokenDto;
    }

    private AuthTokenDto refreshLegacyToken(MemberEntity member, String sessionId, String refreshToken) {
        if (refreshTokenRepository.existsByMemberAndSessionId(member, sessionId)) {
            throw new MarketException(AuthErrorCode.REFRESH_REUSE_EXCEPTION);
        }
        RefreshTokenEntity legacyAuth = new RefreshTokenEntity(member, sessionId, refreshToken);
        refreshTokenRepository.save(legacyAuth);
        return createToken(member);
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
    }
}
