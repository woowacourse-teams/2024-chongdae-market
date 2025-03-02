package com.zzang.chongdae.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zzang.chongdae.auth.repository.AuthRepository;
import com.zzang.chongdae.auth.repository.entity.AuthEntity;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.global.service.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    AuthRepository authRepository;

    @DisplayName("로그인이 성공하면 member_id, refresh_token, device_id가 저장된다.")
    @Test
    void should_saveAuthEntity_whenLoginSuccess() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");

        // when
        authService.kakaoLogin(request);
        List<AuthEntity> actual = authRepository.findAll();

        // then
        assertThat(actual.size()).isEqualTo(1);
    }
}
