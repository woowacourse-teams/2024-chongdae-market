package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.repository.AuthRepository;
import com.zzang.chongdae.auth.repository.entity.AuthEntity;
import com.zzang.chongdae.global.config.WriterDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RefreshTokenValidator {

    @Autowired
    AuthRepository authRepository;

    @WriterDatabase
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(AuthEntity authEntity, String refreshToken) {
        if (!authEntity.isValid(refreshToken)) {
            authRepository.delete(authEntity);
            return false;
        }
        return true;
    }
}
