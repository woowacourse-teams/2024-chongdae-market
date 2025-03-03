package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.repository.RefreshTokenRepository;
import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.global.config.WriterDatabase;
import com.zzang.chongdae.global.exception.MarketException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AuthTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @WriterDatabase
    @Transactional
    public AuthTokenDto createToken(Long memberId) {
        String deviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = createAuthToken(memberId, deviceId);
        RefreshTokenEntity auth = new RefreshTokenEntity(memberId, deviceId, authToken.refreshToken());
        refreshTokenRepository.save(auth);
        return authToken;
    }

    private AuthTokenDto createAuthToken(Long memberId, String deviceId) {
        return jwtTokenProvider.createAuthToken(memberId.toString(), deviceId);
    }

    @WriterDatabase
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(RefreshTokenEntity refreshTokenEntity, String refreshToken) {
        if (!refreshTokenEntity.isValid(refreshToken)) {
            refreshTokenRepository.delete(refreshTokenEntity);
            return false;
        }
        return true;
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refreshLegacyToken(Long memberId, String deviceId, String refreshToken) {
        if (refreshTokenRepository.existsByMemberIdAndDeviceId(memberId, deviceId)) {
            throw new MarketException(AuthErrorCode.REFRESH_REUSE_EXCEPTION);
        }
        RefreshTokenEntity legacyAuth = new RefreshTokenEntity(memberId, deviceId, refreshToken);
        refreshTokenRepository.save(legacyAuth);
        String newDeviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = createAuthToken(memberId, newDeviceId);
        RefreshTokenEntity newAuth = new RefreshTokenEntity(memberId, newDeviceId, authToken.refreshToken());
        refreshTokenRepository.save(newAuth);
        return authToken;
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refresh(RefreshTokenEntity refreshTokenEntity, Long memberId, String deviceId) {
        AuthTokenDto authTokenDto = createAuthToken(memberId, deviceId);
        refreshTokenEntity.refresh(authTokenDto.refreshToken());
        return authTokenDto;
    }
}
