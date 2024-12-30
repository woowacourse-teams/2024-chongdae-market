package com.zzang.chongdae.auth.service;

import com.zzang.chongdae.auth.exception.KakaoLoginExceptionHandler;
import com.zzang.chongdae.auth.service.dto.KakaoLoginResponseDto;
import com.zzang.chongdae.member.domain.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class RealAuthClient implements AuthClient {

    private static final String BEARER_HEADER_FORMAT = "Bearer %s";
    private static final String GET_KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient;

    @Override
    public String getUserInfo(String accessToken) {
        return getKakaoUserInfo(accessToken);
    }

    private String getKakaoUserInfo(String accessToken) {
        KakaoLoginResponseDto responseDto = restClient.get()
                .uri(GET_KAKAO_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, createAuthorization(accessToken))
                .retrieve()
                .onStatus(new KakaoLoginExceptionHandler())
                .body(KakaoLoginResponseDto.class);
        return AuthProvider.KAKAO.buildLoginId(responseDto.id().toString()); // TODO: NPE 처리 고려하기
    }

    private String createAuthorization(String accessToken) {
        return BEARER_HEADER_FORMAT.formatted(accessToken);
    }
}
