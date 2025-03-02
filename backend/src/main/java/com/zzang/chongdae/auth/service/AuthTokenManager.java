package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.AuthErrorCode;
import com.zzang.chongdae.auth.repository.AuthRepository;
import com.zzang.chongdae.auth.repository.entity.AuthEntity;
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

    private final AuthRepository authRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @WriterDatabase
    @Transactional
    public AuthTokenDto createToken(Long memberId) {
        String deviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = createAuthToken(memberId, deviceId);
        AuthEntity auth = new AuthEntity(memberId, deviceId, authToken.refreshToken());
        authRepository.save(auth);
        return authToken;
    }

    private AuthTokenDto createAuthToken(Long memberId, String deviceId) {
        return jwtTokenProvider.createAuthToken(memberId.toString(), deviceId);
    }

    @WriterDatabase
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(AuthEntity authEntity, String refreshToken) {
        if (!authEntity.isValid(refreshToken)) {
            authRepository.delete(authEntity);
            return false;
        }
        return true;
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refreshLegacyToken(Long memberId, String deviceId, String refreshToken) {
        if (authRepository.existsByMemberIdAndDeviceId(memberId, deviceId)) {
            throw new MarketException(AuthErrorCode.REFRESH_REUSE_EXCEPTION);
        }
        AuthEntity legacyAuth = new AuthEntity(memberId, deviceId, refreshToken);
        authRepository.save(legacyAuth);
        String newDeviceId = UUID.randomUUID().toString();
        AuthTokenDto authToken = createAuthToken(memberId, newDeviceId);
        AuthEntity newAuth = new AuthEntity(memberId, newDeviceId, authToken.refreshToken());
        authRepository.save(newAuth);
        return authToken;
    }

    @WriterDatabase
    @Transactional
    public AuthTokenDto refresh(AuthEntity authEntity, Long memberId, String deviceId) {
        AuthTokenDto authTokenDto = createAuthToken(memberId, deviceId);
        authEntity.refresh(authTokenDto.refreshToken());
        return authTokenDto;
    }
}
