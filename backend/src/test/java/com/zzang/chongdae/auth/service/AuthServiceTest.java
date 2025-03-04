package com.zzang.chongdae.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zzang.chongdae.auth.repository.RefreshTokenRepository;
import com.zzang.chongdae.auth.repository.entity.RefreshTokenEntity;
import com.zzang.chongdae.auth.service.dto.AuthInfoDto;
import com.zzang.chongdae.auth.service.dto.AuthTokenDto;
import com.zzang.chongdae.auth.service.dto.KakaoLoginRequest;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("로그인이 성공하면 member_id, refresh_token, device_id가 저장된다.")
    @Test
    void should_saveAuthEntity_whenLoginSuccess() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");

        // when
        authService.kakaoLogin(request);
        List<RefreshTokenEntity> actual = refreshTokenRepository.findAll();

        // then
        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("재사용 하지 않은 토큰일 경우 토큰 재갱신 할 수 있다.")
    @Test
    void should_refreshSuccess_whenRefresh() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");

        // when
        AuthInfoDto authInfoDto = authService.kakaoLogin(request);
        String refreshToken = authInfoDto.authToken().refreshToken();
        clock.plusDays(1);
        AuthTokenDto authToken = authService.refresh(refreshToken);
        String newRefreshToken = authToken.refreshToken();
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(newRefreshToken);
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(newRefreshToken);
        MemberEntity member = memberRepository.findById(memberId).get();
        RefreshTokenEntity auth = refreshTokenRepository.findByMemberAndDeviceId(member, deviceId).get();
        boolean actual = auth.isValid(newRefreshToken);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 재갱신하여 소모한 토큰은 다시 재갱신을 할 수 없다.")
    @Test
    void should_refreshFail_whenRefreshTokenAlreadyUsed() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");

        // when
        AuthInfoDto authInfoDto = authService.kakaoLogin(request);
        String refreshToken = authInfoDto.authToken().refreshToken();
        clock.plusDays(1);
        authService.refresh(refreshToken);

        // then
        assertThatThrownBy(() -> authService.refresh(refreshToken))
                .isInstanceOf(MarketException.class);
    }

    @DisplayName("재갱신 실패 내역이 있을 경우 앞서 재갱신한 토큰을 무효화 한다.")
    @Test
    void should_refreshFail_whenReissueFailureExists() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");
        // when
        AuthInfoDto authInfoDto = authService.kakaoLogin(request);
        String refreshToken = authInfoDto.authToken().refreshToken();
        clock.plusDays(1);
        AuthTokenDto authToken = authService.refresh(refreshToken);
        String newRefreshToken = authToken.refreshToken();
        assertThatExceptionOfType(MarketException.class)
                .isThrownBy(() -> authService.refresh(refreshToken));
        //then
        assertThatThrownBy(() -> authService.refresh(newRefreshToken))
                .isInstanceOf(MarketException.class);
    }


    @DisplayName("기존 버전의 refresh token을 재갱신하면 deviceId가 담긴 새 토큰을 확득할 수 있다.")
    @Test
    void should_refresh_whenRefreshLegacyToken() {
        // given
        memberFixture.createMember("poke");
        String legacyToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxMDA1NjI4MjQwMH0.CQK49O-Z51OKRAyEcwu1A31B4g6cD13HIi35OgB40wM";

        // when
        AuthTokenDto authToken = authService.refresh(legacyToken);
        String newRefreshToken = authToken.refreshToken();
        String deviceId = jwtTokenProvider.getDeviceIdByRefreshToken(newRefreshToken);

        // then
        assertThat(deviceId).isNotNull();
    }

    @DisplayName("기존 버전의 refresh token을 재갱신하고 다시 한번 재갱신하면 예외가 발생한다.")
    @Test
    void should_refreshFail_whenRefreshLegacyTokenTwice() {
        // given
        memberFixture.createMember("poke");
        String legacyToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxMDA1NjI4MjQwMH0.CQK49O-Z51OKRAyEcwu1A31B4g6cD13HIi35OgB40wM";

        // when
        authService.refresh(legacyToken);

        // then
        assertThatThrownBy(() -> authService.refresh(legacyToken))
                .isInstanceOf(MarketException.class);
    }

    @DisplayName("회원이 삭제될 경우 연관된 refresh token들이 제거된다.")
    @Test
    void should_deleteCascade_whenDeleteMemberEntity() {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test", "test");

        // when
        for (int i = 0; i < 3; i++) {
            authService.kakaoLogin(request);
        }
        MemberEntity member = memberRepository.findById(1L).get();
        memberRepository.delete(member);
        List<RefreshTokenEntity> actual = refreshTokenRepository.findAll();

        // then
        assertThat(actual.size()).isEqualTo(0);
    }
}
